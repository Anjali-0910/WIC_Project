package cloud_performance_api.service;

import cloud_performance_api.model.PerformanceRecord;
import cloud_performance_api.repository.PerformanceRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.zip.CRC32;

@Service
public class PerformanceReportService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PerformanceRecordRepository repository;

    public PerformanceReportService(PerformanceRecordRepository repository) {
        this.repository = repository;
    }

    public List<PerformanceRecord> getHistory() { return repository.findAllByOrderByIdDesc(); }

    public Optional<PerformanceRecord> getById(Long id) { return repository.findById(id); }

    public Optional<PerformanceRecord> getLatest() { return repository.findFirstByOrderByIdDesc(); }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    public PerformanceRecord processUpload(MultipartFile file) throws Exception {
        String name = cleanName(file.getOriginalFilename());
        String ext = extensionOf(name);

        Path temp = Files.createTempFile("perf-upload-", ".tmp");
        try {
            file.transferTo(temp);

            PerformanceRecord rec = new PerformanceRecord();
            rec.setFileName(name);
            rec.setFileType(ext.isEmpty() ? "UNKNOWN" : ext.toUpperCase());
            rec.setFileSize(Files.size(temp));
            rec.setSha256(sha256(temp));
            rec.setUploadedAt(LocalDateTime.now());

            JsonNode k6 = ext.equals("json") ? tryReadK6(temp) : null;
            if (k6 != null) {
                rec.setTestType("K6_REPORT");
                fillFromK6(rec, k6);
            } else {
                rec.setTestType("FILE_BENCHMARK");
                benchmark(rec, temp);
            }

            applyScore(rec);
            return repository.save(rec);
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    private JsonNode tryReadK6(Path p) {
        try {
            JsonNode root = objectMapper.readTree(p.toFile());
            JsonNode m = root.get("metrics");
            if (m != null && m.has("http_reqs") && m.has("http_req_duration")) return root;
        } catch (Exception ignored) { }
        return null;
    }

    private JsonNode vals(JsonNode node) {
        if (node == null) return objectMapper.missingNode();
        return node.has("values") ? node.get("values") : node;
    }

    private void fillFromK6(PerformanceRecord rec, JsonNode root) {
        JsonNode m = root.get("metrics");
        JsonNode reqs = vals(m.get("http_reqs"));
        JsonNode dur = vals(m.get("http_req_duration"));

        long total = reqs.path("count").asLong();
        double rate = reqs.path("rate").asDouble();

        rec.setTotalRequests((int) total);
        rec.setRequestsPerSecond(round(rate, 2));
        rec.setAverageResponseTime(round(dur.path("avg").asDouble(), 2));
        rec.setMinResponseTime(round(dur.path("min").asDouble(), 2));
        rec.setP95ResponseTime(round(dur.path("p(95)").asDouble(), 2));
        rec.setMaxResponseTime(round(dur.path("max").asDouble(), 2));
        rec.setErrorRate(round(readErrorRate(m.get("http_req_failed")), 4));
        rec.setDurationSeconds(rate > 0 ? round(total / rate, 2) : 0);

        JsonNode vus = vals(m.get("vus_max"));
        int v = Math.max(vus.path("value").asInt(), vus.path("max").asInt());
        if (v == 0) {
            JsonNode vn = vals(m.get("vus"));
            v = Math.max(vn.path("value").asInt(), vn.path("max").asInt());
        }
        rec.setVirtualUsers(Math.max(1, v));
    }

    private double readErrorRate(JsonNode node) {
        if (node == null) return 0;
        JsonNode v = vals(node);
        if (v.has("value")) return v.get("value").asDouble();
        if (v.has("rate")) return v.get("rate").asDouble();
        double passes = v.path("passes").asDouble();
        double fails = v.path("fails").asDouble();
        return (passes + fails) > 0 ? passes / (passes + fails) : 0;
    }

    private void benchmark(PerformanceRecord rec, Path file) {
        long size = rec.getFileSize();
        int runs = size <= 20L * 1024 * 1024 ? 20 : size <= 100L * 1024 * 1024 ? 10 : 5;

        List<Double> times = new ArrayList<>();
        int errors = 0;
        long expected = -1;
        byte[] buf = new byte[64 * 1024];

        long wallStart = System.nanoTime();
        for (int i = 0; i < runs; i++) {
            long t0 = System.nanoTime();
            try (InputStream in = Files.newInputStream(file)) {
                CRC32 crc = new CRC32();
                int n;
                while ((n = in.read(buf)) != -1) crc.update(buf, 0, n);
                long value = crc.getValue();
                if (expected == -1) expected = value;
                else if (value != expected) errors++;
            } catch (IOException e) {
                errors++;
            }
            times.add((System.nanoTime() - t0) / 1_000_000.0);
        }
        double wallSec = (System.nanoTime() - wallStart) / 1_000_000_000.0;

        Collections.sort(times);
        double sum = 0;
        for (double t : times) sum += t;
        int p95Index = Math.max(0, (int) Math.ceil(0.95 * times.size()) - 1);

        rec.setTotalRequests(runs);
        rec.setErrorRate(round(errors / (double) runs, 4));
        rec.setAverageResponseTime(round(sum / times.size(), 3));
        rec.setMinResponseTime(round(times.get(0), 3));
        rec.setP95ResponseTime(round(times.get(p95Index), 3));
        rec.setMaxResponseTime(round(times.get(times.size() - 1), 3));
        rec.setRequestsPerSecond(round(wallSec > 0 ? runs / wallSec : 0, 2));
        rec.setDurationSeconds(round(wallSec, 3));
        rec.setVirtualUsers(1);
    }

    private void applyScore(PerformanceRecord r) {
        double responseTimeScore = 100.0;
        double reliabilityScore = 100.0;
        double throughputScore = 100.0;

        if (r.getP95ResponseTime() > 500) responseTimeScore -= 40;
        else if (r.getP95ResponseTime() > 200) responseTimeScore -= 20;
        else if (r.getP95ResponseTime() > 100) responseTimeScore -= 10;

        if (r.getErrorRate() > 0.05) reliabilityScore -= 30;
        else if (r.getErrorRate() > 0.01) reliabilityScore -= 15;

        if (r.getRequestsPerSecond() < 5) throughputScore -= 30;
        else if (r.getRequestsPerSecond() < 10) throughputScore -= 15;

        double score = round((responseTimeScore + reliabilityScore + throughputScore) / 3.0, 1);

        String rating;
        if (score >= 90) rating = "Excellent";
        else if (score >= 75) rating = "Good";
        else if (score >= 50) rating = "Needs Improvement";
        else rating = "Poor";

        r.setResponseTimeScore(responseTimeScore);
        r.setReliabilityScore(reliabilityScore);
        r.setThroughputScore(throughputScore);
        r.setScore(score);
        r.setRating(rating);
    }

    private String sha256(Path p) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream in = Files.newInputStream(p)) {
            byte[] buf = new byte[64 * 1024];
            int n;
            while ((n = in.read(buf)) != -1) md.update(buf, 0, n);
        }
        return HexFormat.of().formatHex(md.digest());
    }

    private String cleanName(String original) {
        String n = original == null ? "unnamed" : original;
        n = n.substring(Math.max(n.lastIndexOf('/'), n.lastIndexOf('\\')) + 1).trim();
        if (n.isEmpty()) n = "unnamed";
        if (n.length() > 255) n = n.substring(n.length() - 255);
        return n;
    }

    private String extensionOf(String name) {
        int i = name.lastIndexOf('.');
        if (i < 0 || i == name.length() - 1) return "";
        return name.substring(i + 1).toLowerCase();
    }

    private static double round(double v, int places) {
        double f = Math.pow(10, places);
        return Math.round(v * f) / f;
    }
}
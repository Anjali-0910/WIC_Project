package cloud_performance_api.controller;

import cloud_performance_api.model.PerformanceRecord;
import cloud_performance_api.service.PerformanceReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceReportService service;

    public PerformanceController(PerformanceReportService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No file was uploaded, or the file is empty."));
        }
        try {
            return ResponseEntity.ok(service.processUpload(file));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Could not analyse file: " + String.valueOf(e.getMessage())));
        }
    }

    @GetMapping("/history")
    public List<PerformanceRecord> history() {
        return service.getHistory();
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<PerformanceRecord> byId(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/latest")
    public ResponseEntity<PerformanceRecord> latest() {
        return service.getLatest().map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
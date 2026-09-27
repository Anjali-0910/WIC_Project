package cloud_performance_api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class PerformanceUploadController {

    @Value("${performance.report.path:performance-reports/k6-summary.json}")
    private String reportPath;

    @PostMapping("/api/performance/upload")
    public ResponseEntity<String> uploadReport(@RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file was uploaded.");
        }

        Path target = Path.of(reportPath);

        if (target.getParent() != null) {
            Files.createDirectories(target.getParent());
        }

        file.transferTo(target);

        return ResponseEntity.ok("Report uploaded successfully.");
    }
}

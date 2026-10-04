package cloud_performance_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "performance_records")
public class PerformanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String testType;
    private long fileSize;
    private String sha256;
    private LocalDateTime uploadedAt;

    private int totalRequests;
    private double errorRate;
    private double averageResponseTime;
    private double minResponseTime;
    private double p95ResponseTime;
    private double maxResponseTime;
    private double requestsPerSecond;
    private double durationSeconds;
    private int virtualUsers;

    private double score;
    private String rating;
    private double responseTimeScore;
    private double reliabilityScore;
    private double throughputScore;

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public void setFileName(String v) { this.fileName = v; }
    public String getFileType() { return fileType; }
    public void setFileType(String v) { this.fileType = v; }
    public String getTestType() { return testType; }
    public void setTestType(String v) { this.testType = v; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long v) { this.fileSize = v; }
    public String getSha256() { return sha256; }
    public void setSha256(String v) { this.sha256 = v; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime v) { this.uploadedAt = v; }
    public int getTotalRequests() { return totalRequests; }
    public void setTotalRequests(int v) { this.totalRequests = v; }
    public double getErrorRate() { return errorRate; }
    public void setErrorRate(double v) { this.errorRate = v; }
    public double getAverageResponseTime() { return averageResponseTime; }
    public void setAverageResponseTime(double v) { this.averageResponseTime = v; }
    public double getMinResponseTime() { return minResponseTime; }
    public void setMinResponseTime(double v) { this.minResponseTime = v; }
    public double getP95ResponseTime() { return p95ResponseTime; }
    public void setP95ResponseTime(double v) { this.p95ResponseTime = v; }
    public double getMaxResponseTime() { return maxResponseTime; }
    public void setMaxResponseTime(double v) { this.maxResponseTime = v; }
    public double getRequestsPerSecond() { return requestsPerSecond; }
    public void setRequestsPerSecond(double v) { this.requestsPerSecond = v; }
    public double getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(double v) { this.durationSeconds = v; }
    public int getVirtualUsers() { return virtualUsers; }
    public void setVirtualUsers(int v) { this.virtualUsers = v; }
    public double getScore() { return score; }
    public void setScore(double v) { this.score = v; }
    public String getRating() { return rating; }
    public void setRating(String v) { this.rating = v; }
    public double getResponseTimeScore() { return responseTimeScore; }
    public void setResponseTimeScore(double v) { this.responseTimeScore = v; }
    public double getReliabilityScore() { return reliabilityScore; }
    public void setReliabilityScore(double v) { this.reliabilityScore = v; }
    public double getThroughputScore() { return throughputScore; }
    public void setThroughputScore(double v) { this.throughputScore = v; }
}
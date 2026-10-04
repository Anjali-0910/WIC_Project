package cloud_performance_api.repository;

import cloud_performance_api.model.PerformanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerformanceRecordRepository extends JpaRepository<PerformanceRecord, Long> {

    List<PerformanceRecord> findAllByOrderByIdDesc();

    Optional<PerformanceRecord> findFirstByOrderByIdDesc();
}
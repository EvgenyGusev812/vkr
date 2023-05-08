package spbpu.clinic.vkr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spbpu.clinic.vkr.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}

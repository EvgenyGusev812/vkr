package spbpu.clinic.vkr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spbpu.clinic.vkr.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}

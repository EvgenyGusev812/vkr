package spbpu.clinic.vkr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spbpu.clinic.vkr.entity.TypeOfMedicalCare;

@Repository
public interface TypeOfMedicalCareRepository extends JpaRepository<TypeOfMedicalCare, Long> {
}

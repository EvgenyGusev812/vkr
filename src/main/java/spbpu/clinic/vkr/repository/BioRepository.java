package spbpu.clinic.vkr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spbpu.clinic.vkr.entity.Bio;

@Repository
public interface BioRepository extends JpaRepository<Bio, Long> {
}

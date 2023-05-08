package spbpu.clinic.vkr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spbpu.clinic.vkr.entity.Doctor;
import spbpu.clinic.vkr.entity.User;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Doctor findByUser(User user);
}

package spbpu.clinic.vkr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spbpu.clinic.vkr.entity.Appointment;
import spbpu.clinic.vkr.entity.Doctor;
import spbpu.clinic.vkr.entity.Patient;
import spbpu.clinic.vkr.entity.TypeOfMedicalCare;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByPatient(Patient patient);

    List<Appointment> findAllByDoctor(Doctor doctor);

    List<Appointment> findAllByTypeOfMedicalCare(TypeOfMedicalCare typeOfMedicalCare);

}

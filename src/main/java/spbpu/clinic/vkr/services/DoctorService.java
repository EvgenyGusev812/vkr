package spbpu.clinic.vkr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spbpu.clinic.vkr.entity.Doctor;
import spbpu.clinic.vkr.entity.User;
import spbpu.clinic.vkr.repository.DoctorRepository;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getById(Long id) {
        return doctorRepository.getById(id);
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor getByUser(User user) {
        return doctorRepository.findByUser(user);
    }

    public void deleteById(Long id) {
        doctorRepository.deleteById(id);
    }
}

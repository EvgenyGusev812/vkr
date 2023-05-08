package spbpu.clinic.vkr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spbpu.clinic.vkr.entity.*;
import spbpu.clinic.vkr.repository.AppointmentRepository;
import spbpu.clinic.vkr.repository.PatientRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TypeOfMedicalCareService typeOfMedicalCareService;

    @Autowired
    private DoctorService doctorService;

    public Page<Appointment> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return appointmentRepository.findAll(pageable);
    }


    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> findAllByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        return appointmentRepository.findAllByPatient(patient);
    }

    public List<Appointment> findAllByDoctor(Long doctorId) {
        Doctor doctor = doctorService.getById(doctorId);
        if (doctor != null) {
            return appointmentRepository.findAllByDoctor(doctor);
        }
        return Collections.emptyList();
    }

    public List<Appointment> findAllByTypeOfMedicalCareId(Long typeOfMedicalCareId) {
        TypeOfMedicalCare typeOfMedicalCare = typeOfMedicalCareService.getById(typeOfMedicalCareId);
        if (typeOfMedicalCare != null) {
            return appointmentRepository.findAllByTypeOfMedicalCare(typeOfMedicalCare);
        }
        return Collections.emptyList();
    }

    public void deleteList(List<Appointment> appointments) {
        appointmentRepository.deleteAll(appointments);
    }

    public Appointment getById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> findAllByKeyword(String keyword) {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> result = new ArrayList<>();
        appointments.forEach(e -> {
            if (String.valueOf(e.getId()).equals(keyword)) {
                result.add(e);
                return;
            }
            Bio bio = e.getPatient().getBio();
            if (bio.getName().toLowerCase().contains(keyword)
                    || bio.getSurname().toLowerCase().contains(keyword)
                    || bio.getMiddlename().toLowerCase().contains(keyword)) {
                result.add(e);
                return;
            }
            if (e.getDate() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = formatter.format(e.getDate());
                if (dateStr.contains(keyword)) {
                    result.add(e);
                }
            }
        });
        return result;
    }


}

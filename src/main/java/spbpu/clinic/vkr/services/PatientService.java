package spbpu.clinic.vkr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import spbpu.clinic.vkr.entity.Appointment;
import spbpu.clinic.vkr.entity.Bio;
import spbpu.clinic.vkr.entity.Patient;
import spbpu.clinic.vkr.entity.TypeOfMedicalCare;
import spbpu.clinic.vkr.repository.AppointmentRepository;
import spbpu.clinic.vkr.repository.PatientRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient getById(Long id) {
        return patientRepository.findById(id).orElse(new Patient());
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void delete(Long id) {
        Patient patient = patientRepository.getById(id);
        List<Appointment> appointments = appointmentRepository.findAllByPatient(patient);
        if (!CollectionUtils.isEmpty(appointments)) {
            appointmentRepository.deleteAll(appointments);
        }
        patientRepository.deleteById(id);

    }

    public Page<Patient> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return patientRepository.findAll(pageable);
    }

    public List<Patient> findAllByKeyword(String keyword) {
        List<Patient> appointments = patientRepository.findAll();
        List<Patient> result = new ArrayList<>();
        appointments.forEach(e -> {
            Bio bio = e.getBio();
            if (bio.getName().toLowerCase().contains(keyword)
                    || bio.getSurname().toLowerCase().contains(keyword)
                    || bio.getMiddlename().toLowerCase().contains(keyword)) {
                result.add(e);
                return;
            }
            if (bio.getBirthdate() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = formatter.format(bio.getBirthdate());
                if (dateStr.contains(keyword)) {
                    result.add(e);
                }
            }
            if (bio.getOmsPolicy() != null && bio.getOmsPolicy().toLowerCase().contains(keyword)) {
                result.add(e);
                return;
            }
            if (bio.getPhoneNumber() != null &&  bio.getPhoneNumber().toLowerCase().contains(keyword)) {
                result.add(e);
                return;
            }
            if (bio.getMail() != null && bio.getMail().toLowerCase().contains(keyword)) {
                result.add(e);
            }
        });
        return result;
    }
}

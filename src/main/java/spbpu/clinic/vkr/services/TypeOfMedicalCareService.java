package spbpu.clinic.vkr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import spbpu.clinic.vkr.entity.Appointment;
import spbpu.clinic.vkr.entity.TypeOfMedicalCare;
import spbpu.clinic.vkr.repository.TypeOfMedicalCareRepository;

import java.util.List;

@Service
public class TypeOfMedicalCareService {

    @Autowired
    private TypeOfMedicalCareRepository typeOfMedicalCareRepository;

    @Autowired
    private AppointmentService appointmentService;

    public Page<TypeOfMedicalCare> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,5);
        return typeOfMedicalCareRepository.findAll(pageable);
    }

    public List<TypeOfMedicalCare> findAll() {
        return typeOfMedicalCareRepository.findAll();
    }

    public TypeOfMedicalCare getById(Long id) {
        return typeOfMedicalCareRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        List<Appointment> appointments = appointmentService.findAllByTypeOfMedicalCareId(id);
        if (!CollectionUtils.isEmpty(appointments)) {
            appointmentService.deleteList(appointments);
        }
        typeOfMedicalCareRepository.deleteById(id);
    }

    public TypeOfMedicalCare saveMedicalCare(TypeOfMedicalCare typeOfMedicalCare) {
        return typeOfMedicalCareRepository.save(typeOfMedicalCare);
    }

}

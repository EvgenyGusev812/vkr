package spbpu.clinic.vkr.mapper;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spbpu.clinic.vkr.entity.Appointment;
import spbpu.clinic.vkr.services.DoctorService;
import spbpu.clinic.vkr.services.PatientService;
import spbpu.clinic.vkr.services.TypeOfMedicalCareService;
import spbpu.clinic.vkr.types.SelectedItem;
import spbpu.clinic.vkr.wrapper.AppointmentWrapper;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

@Service
public class AppointmentMapper {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private TypeOfMedicalCareService typeOfMedicalCareService;

    @SneakyThrows
    public AppointmentWrapper toWrapper(Appointment appointment) {
        AppointmentWrapper wrapper = new AppointmentWrapper();
        if (appointment.getDate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String newDateStr = simpleDateFormat.format(appointment.getDate());
            wrapper.setDate(simpleDateFormat.parse(newDateStr));
        }
        wrapper.setId(appointment.getId());
        wrapper.setDiagnosis(appointment.getDiagnosis());
        wrapper.setDescription(appointment.getDescription());
        if (appointment.getPatient() != null) {
            wrapper.setPatient(String.valueOf(appointment.getPatient()));
        }
        if (appointment.getDoctor() != null) {
            wrapper.setDoctor(String.valueOf(appointment.getDoctor()));
        }
        if (appointment.getTypeOfMedicalCare() != null) {
            wrapper.setTypeOfMedicalCare(String.valueOf(appointment.getTypeOfMedicalCare()));
        }
        return wrapper;
    }

    public Appointment toEntity(AppointmentWrapper wrapper) {
        Appointment appointment = new Appointment();
        appointment.setId(wrapper.getId());
        appointment.setDescription(wrapper.getDescription());
        appointment.setDiagnosis(wrapper.getDiagnosis());
        appointment.setDate(wrapper.getDate());
        if (wrapper.getPatient() != null) {
            appointment.setPatient(patientService.getById(convertToId(wrapper.getPatient())));
        }
        if (wrapper.getDoctor() != null) {
            appointment.setDoctor(doctorService.getById(convertToId(wrapper.getDoctor())));
        }
        if (wrapper.getTypeOfMedicalCare() != null) {
            appointment.setTypeOfMedicalCare(typeOfMedicalCareService.getById(convertToId(wrapper.getTypeOfMedicalCare())));
        }
        return appointment;
    }

    private Long convertToId(String str) {
        int index = str.indexOf(",");
        if (index == -1) {
            return Long.valueOf(str);
        } else {
            return Long.valueOf(str.substring(0, index));
        }
    }
}

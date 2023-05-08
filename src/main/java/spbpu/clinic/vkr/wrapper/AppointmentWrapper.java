package spbpu.clinic.vkr.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentWrapper {

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date;

    private String patient;

    private String doctor;

    private String description;

    private String diagnosis;

    private String typeOfMedicalCare;
}

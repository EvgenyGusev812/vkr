package spbpu.clinic.vkr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "APPOINTMENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE")
    private Date date;

    @OneToOne
    @JoinColumn(name = "patiend_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "type_of_medical_care_id", referencedColumnName = "id")
    private TypeOfMedicalCare typeOfMedicalCare;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "DIAGNOSIS", columnDefinition = "TEXT")
    private String diagnosis;

}

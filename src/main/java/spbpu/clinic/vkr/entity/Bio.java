package spbpu.clinic.vkr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "MIDDLENAME")
    private String middlename;

    @Column(name = "PHONENUMBER")
    private String phoneNumber;

    @Column(name = "MAIL")
    private String mail;

    @Column(name = "OMSPOLICY")
    private String omsPolicy;

    @Column(name = "BIRTHDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;



}

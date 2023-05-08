package spbpu.clinic.vkr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TYPEOFMEDICALCARE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfMedicalCare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "LABEL")
    private String label;


}

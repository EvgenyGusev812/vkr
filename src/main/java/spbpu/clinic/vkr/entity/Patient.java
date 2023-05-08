package spbpu.clinic.vkr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PATIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bio_id", referencedColumnName = "id")
    private Bio bio;



}
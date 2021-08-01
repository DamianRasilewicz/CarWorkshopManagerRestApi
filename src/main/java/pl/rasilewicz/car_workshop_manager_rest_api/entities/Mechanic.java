package pl.rasilewicz.car_workshop_manager_rest_api.entities;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "mechanics")
public class Mechanic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String firstName;
    private String lastName;
    private String speciality;
    private Integer seniority;

    @ManyToOne()
    @JoinColumn(name = "workshop_id")
    private Workshop workshop;

}

package pl.rasilewicz.car_workshop_manager_rest_api.entities;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

}

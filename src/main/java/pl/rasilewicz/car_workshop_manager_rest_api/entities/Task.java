package pl.rasilewicz.car_workshop_manager_rest_api.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String description;
    private Double estimatedExecutionTime;
    private Integer estimatedCost;

    public Task(){};
}

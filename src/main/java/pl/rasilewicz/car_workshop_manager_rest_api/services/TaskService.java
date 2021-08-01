package pl.rasilewicz.car_workshop_manager_rest_api.services;

import pl.rasilewicz.car_workshop_manager_rest_api.entities.Task;

import java.util.List;

public interface TaskService {

    List<Task> findAllTasks();

    Task findTaskById(Integer id);
}

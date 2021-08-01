package pl.rasilewicz.car_workshop_manager_rest_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Task;
import pl.rasilewicz.car_workshop_manager_rest_api.repositories.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAllTasks();
    }

    @Override
    public Task findTaskById(Integer id) {
        return taskRepository.findTaskById(id);
    }
}

package pl.rasilewicz.car_workshop_manager_rest_api.repositories;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Task;
import java.util.List;

@Repository
@EntityScan(basePackages = "pl.rasilewicz.car_workshop_manager.entities")
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT * FROM car_workshop_manager.tasks", nativeQuery = true)
    List<Task> findAllTasks();

    Task findTaskById(Integer id);
}

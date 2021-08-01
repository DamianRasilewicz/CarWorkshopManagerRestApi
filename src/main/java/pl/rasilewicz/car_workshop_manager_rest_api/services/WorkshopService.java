package pl.rasilewicz.car_workshop_manager_rest_api.services;

import pl.rasilewicz.car_workshop_manager_rest_api.entities.Workshop;

import java.util.List;

public interface WorkshopService {

    List<Workshop> findAllWorkshops();

    Workshop findWorkshopById(Integer id);
}

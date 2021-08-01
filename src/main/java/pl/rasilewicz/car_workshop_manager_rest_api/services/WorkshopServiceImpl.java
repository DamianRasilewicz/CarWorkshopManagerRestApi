package pl.rasilewicz.car_workshop_manager_rest_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Workshop;
import pl.rasilewicz.car_workshop_manager_rest_api.repositories.WorkshopRepository;

import java.util.List;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;

    @Autowired
    public WorkshopServiceImpl(WorkshopRepository workshopRepository){
        this.workshopRepository = workshopRepository;
    }

    @Override
    public List<Workshop> findAllWorkshops() {
        return workshopRepository.findAllWorkshops();
    }

    @Override
    public Workshop findWorkshopById(Integer id) {
        return workshopRepository.findWorkshopById(id);
    }
}

package pl.rasilewicz.car_workshop_manager_rest_api.services;

import pl.rasilewicz.car_workshop_manager_rest_api.entities.VisitDate;

import java.time.LocalDate;
import java.util.List;

public interface VisitDateService {

    List<VisitDate> findVisitDateByDateAndWorkshopId(LocalDate date, Integer workshopId);

    void save (VisitDate visitDate);

    VisitDate findVisitDateById(Integer id);

    Integer findNumberOfVisitDatesByMonthByUserId(Integer month, Integer userId);

    Integer findNumberOfVisitDatesAllUsersOfYear(Integer month, Integer year);

}

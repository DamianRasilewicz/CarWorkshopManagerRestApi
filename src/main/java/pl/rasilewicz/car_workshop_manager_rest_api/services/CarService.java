package pl.rasilewicz.car_workshop_manager_rest_api.services;


import pl.rasilewicz.car_workshop_manager_rest_api.entities.Car;

import java.util.List;

public interface CarService {

    void save (Car car);

    Car findCarById(Integer id);

    List<Car> findCarsByUserId(Integer userId);

    void deleteById (Integer id);

    Car editCar(Car car);

}

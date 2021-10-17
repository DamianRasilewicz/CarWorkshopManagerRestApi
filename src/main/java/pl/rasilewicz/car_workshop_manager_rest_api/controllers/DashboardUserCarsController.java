package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Car;
import pl.rasilewicz.car_workshop_manager_rest_api.services.CarServiceImpl;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardUserCarsController {

    private final CarServiceImpl carService;

    @GetMapping("/users/{userId}/cars")
    public List<Car> userCarsList (@PathVariable Integer userId){
        List<Car> userCarList = carService.findCarsByUserId((userId));

        return userCarList;
    }

    @GetMapping("/users/{userId}/cars/{carId}/edit")
    public Car userCarView (@PathVariable Integer carId){
        Car editingCar = carService.findCarById(carId);

        return editingCar;
    }

    @PutMapping("/users/{userId}/cars/{carId}/edit")
    public Car editedUserCar (@RequestBody Car car){

        return carService.editCar(car);
    }

    @DeleteMapping("users/{userId}/cars/{carId}/delete")
    public void DeleteCar (@PathVariable Integer carId){

        carService.deleteById(carId);
    }
}

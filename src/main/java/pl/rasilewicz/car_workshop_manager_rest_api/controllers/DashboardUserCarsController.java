package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Car;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.CarServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;
import java.util.List;

import javax.servlet.http.HttpSession;

@RestController
@Controller
public class DashboardUserCarsController {

    private final UserServiceImpl userService;
    private final CarServiceImpl carService;

    public DashboardUserCarsController(UserServiceImpl userService, CarServiceImpl carService){
        this.userService = userService;
        this.carService = carService;
    }

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

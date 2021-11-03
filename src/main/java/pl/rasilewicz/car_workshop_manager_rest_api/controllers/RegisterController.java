package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.RoleServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final RoleServiceImpl roleService;
    private final UserServiceImpl userService;

    @GetMapping("/registration")
    public User registerForm(){

        return new User();
    }

    @PostMapping("/registration")
    public User registerFormFilled(@RequestBody User user){

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        user.setRegistered(true);
        user.setEnabled(true);
        LocalDate registeredDate = LocalDate.now();
        user.setRegisteredDate(registeredDate.toString());
        user.setRegisteredDay(String.valueOf(registeredDate.getDayOfMonth()));
        user.setRegisteredMonth(String.valueOf(registeredDate.getMonthValue()));
        user.setRegisteredYear(String.valueOf(registeredDate.getYear()));

        Role userRole = roleService.findRoleByName("USER");
        user.setRole(userRole);

        userService.save(user);

        return user;
    }
}

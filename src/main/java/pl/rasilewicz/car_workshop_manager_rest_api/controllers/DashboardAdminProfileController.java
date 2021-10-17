package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;

@RestController
@RequiredArgsConstructor
public class DashboardAdminProfileController {

    private final UserServiceImpl userService;


    @GetMapping("/admins/{userId}/profile")
    public User GetAdminProfile (@PathVariable Integer userId){


        return userService.findUserById(userId);
    }

    @PutMapping("/admins/{userId}profile")
    public User editAdminProfile (@RequestBody User user){


        return userService.editProfile(user);
    }
}

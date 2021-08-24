package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.RoleServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;


import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class DashboardAdminProfileController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;


    @GetMapping("/admins/{userId}/profile")
    public User GetAdminProfile (@PathVariable Integer userId){


        return userService.findUserById(userId);
    }

    @PutMapping("/admins/{userId}/profile")
    public User editAdminProfile (@PathVariable Integer userId, @RequestBody User editedProfile){


        return userService.editProfile(editedProfile);
    }
}

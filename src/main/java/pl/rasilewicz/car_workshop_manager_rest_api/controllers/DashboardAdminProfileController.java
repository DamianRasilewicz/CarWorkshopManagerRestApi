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
        User userProfile = userService.findUserById(userId);

        return userProfile;
    }

    @PutMapping("/admin/profiles")
    public User changedAdminProfile (@RequestBody User userProfile, HttpSession session){


        Role userRole = roleService.findRoleByName("USER");
        userProfile.setRole(userRole);
        userService.save(userProfile);

        session.removeAttribute("userName");
        session.setAttribute("userName", userProfile.getUserName());

        return userProfile;
    }
}

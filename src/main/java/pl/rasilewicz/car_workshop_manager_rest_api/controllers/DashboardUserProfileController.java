package pl.rasilewicz.car_workshop_manager_rest_api.controllers;


import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.RoleServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class DashboardUserProfileController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @GetMapping("/users/{userId}/profile")
    public User userProfile (@PathVariable Integer userId){

        return userService.findUserById(userId);
    }

    @PutMapping("/users/{userId}/profile")
    public User changedUserProfile (@RequestBody User user){

        User userProfile = userService.findUserById(user.getId());

        if (!user.getPassword().equals(userProfile.getPassword())){
            userProfile.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        }

        Role userRole = roleService.findRoleByName("USER");
        userProfile.setRole(userRole);
        userProfile.setPhoneNumber(user.getPhoneNumber());
        userProfile.setEmail(user.getEmail());
        userProfile.setLastName(user.getLastName());
        userProfile.setFirstName(user.getFirstName());
        userProfile.setUserName(user.getUserName());
        userService.save(userProfile);

        return userProfile;
    }
}

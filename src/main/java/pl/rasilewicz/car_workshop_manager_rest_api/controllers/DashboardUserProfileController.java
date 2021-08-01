package pl.rasilewicz.car_workshop_manager_rest_api.controllers;


import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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


    @GetMapping("/dashboard/user/profile")
    public String userProfile (Model model, HttpSession session){
        User userProfile = userService.findUserById((Integer)session.getAttribute("userId"));
        model.addAttribute("userProfile", userProfile);

        String newPassword = "";
        model.addAttribute("newPassword", newPassword);

        return "dashboardPages/user/userProfile";
    }

    @PostMapping("/dashboard/user/profile")
    public String changedUserProfile (User userProfile, String newPassword, HttpSession session){

        if (!newPassword.equals("")){
            userProfile.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
        } else {
            userProfile.setPassword(userProfile.getPassword());
        }
        Role userRole = roleService.findRoleByName("USER");
        userProfile.setRole(userRole);
        userService.save(userProfile);

        session.removeAttribute("userName");
        session.setAttribute("userName", userProfile.getUserName());

        return "redirect:/dashboard/user/profile?success";
    }
}
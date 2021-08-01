package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.RoleServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
public class RegisterController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final RoleServiceImpl roleService;
    private final UserServiceImpl userService;

    public RegisterController(RoleServiceImpl roleService, UserServiceImpl userService){
        this.roleService = roleService;
        this.userService = userService;

    }

    @GetMapping("/registration")
    public String registerForm(Model model){
        model.addAttribute("user", new User());
        return "registrationPage/registrationForm";
    }

    @PostMapping("/registration")
    public String registerFormFilled(@ModelAttribute("user") @Valid User user, BindingResult result){
        if (result.hasErrors()) {
            return "registrationPage/registrationForm";
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        user.setRegistered(true);
        user.setEnabled(true);
        LocalDate registeredDate = LocalDate.now();
        user.setRegisteredDate(registeredDate);
        user.setRegisteredDay(registeredDate.getDayOfMonth());
        user.setRegisteredMonth(registeredDate.getMonthValue());
        user.setRegisteredYear(registeredDate.getYear());

        Role userRole = roleService.findRoleByName("USER");
        user.setRole(userRole);

        userService.save(user);

        return "redirect:/registration?success";
    }
}

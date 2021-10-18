package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("user", new User());
        return "loginPage/login";
    }

    @PostMapping("/login")
    public User loggedUser (@ModelAttribute("user") User user, @RequestParam String userName, @RequestParam String password, BindingResult result, HttpSession session) {

        if (userService.findByUsername(userName) == null) {
            System.out.println("incorrect userName!");
        }

        User inputtedUser = userService.findByUsername(userName);

        if (inputtedUser.getPassword().equals(password)) {
            session.setAttribute("userName", inputtedUser.getUsername());
            session.setAttribute("userId", inputtedUser.getId());
            if (inputtedUser.getRole().getName().equals("USER")) {
                return null;
            } else {
                return null;
            }
        }else {
            System.out.println("Wrong password");
        }
        return inputtedUser;
    }


    @GetMapping("/logout")
    public String logout(SessionStatus session, HttpSession httpSession) {
        session.setComplete();
        httpSession.removeAttribute("userName");
        httpSession.removeAttribute("userId");
        return "redirect:/";
    }
}

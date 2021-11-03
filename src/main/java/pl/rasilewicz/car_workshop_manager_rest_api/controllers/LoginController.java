package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.rasilewicz.car_workshop_manager_rest_api.configuration.LoginCredentials;

@RestController
@RequiredArgsConstructor
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentials credentials){

    }
}

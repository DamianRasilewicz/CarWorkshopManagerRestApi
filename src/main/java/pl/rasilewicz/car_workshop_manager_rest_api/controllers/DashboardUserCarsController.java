package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Car;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.CarServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;
import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
public class DashboardUserCarsController {

    private final UserServiceImpl userService;
    private final CarServiceImpl carService;

    public DashboardUserCarsController(UserServiceImpl userService, CarServiceImpl carService){
        this.userService = userService;
        this.carService = carService;
    }

    @GetMapping("/dashboard/user/cars")
    public String userCarsList (Model model, HttpSession session){
        List<Car> userCarList = carService.findCarsByUserId((Integer)session.getAttribute("userId"));
        model.addAttribute("userCarList", userCarList);

        return "dashboardPages/user/cars";
    }

    @GetMapping("/dashboard/user/cars/edit")
    public String userCarEditing (@RequestParam Integer id, Model model){
        Car editingCar = carService.findCarById(id);
        model.addAttribute("editingCar", editingCar);

        return "dashboardPages/user/carEdit";
    }

    @PostMapping("/dashboard/user/cars/edit")
    public String editedUserCar (Car editingCar, HttpSession session, RedirectAttributes redirectAttributes){
        User user = userService.findUserById((Integer) session.getAttribute("userId"));
        editingCar.setUser(user);
        carService.save(editingCar);

        redirectAttributes.addAttribute("id", editingCar.getId());

        return "redirect:/dashboard/user/cars/edit?success";
    }

    @GetMapping("/dashboard/user/cars/delete")
    public String viewingConfirmViewDeleteCar (@RequestParam Integer id, Model model){
        Car selectedCar = carService.findCarById(id);
        model.addAttribute("selectedCar", selectedCar);
        model.addAttribute("id", id);

        return "dashboardPages/user/confirmationDeleteCar";
    }

    @PostMapping("/dashboard/user/cars/delete")
    public String afterConfirmedBoxDeleteCar (Integer id){
        carService.deleteById(id);

        return "redirect:/dashboard/user/cars?carDeleteSuccess";
    }
}

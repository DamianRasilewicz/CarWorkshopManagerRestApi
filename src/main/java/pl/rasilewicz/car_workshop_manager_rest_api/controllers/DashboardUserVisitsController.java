package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.services.CarServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.OrderServiceImpl;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardUserVisitsController {

    private final OrderServiceImpl orderService;
    private final CarServiceImpl carService;

    @GetMapping("/users/{userId}/visits")
    public List<Order> userVisits (@PathVariable Integer userId){

        return orderService.findOrdersByUserId(userId);
    }

    @GetMapping("/users/{userId}/visits/{visitId}/details")
    public Order viewingSelectedVisit (@PathVariable Integer visitId){

        return orderService.findOrderById(visitId);
    }

    @PutMapping("/users/{userId}/visits/{visitId}/details")
    public Order changedSelectedVisit (@RequestBody Order order) {

        return  orderService.editOrder(order);
    }

    @DeleteMapping("/users/{userId}/lastVisits/{visitId}/delete")
    public void DeleteLastVisit (@PathVariable Integer visitId){

        orderService.deleteById(visitId);

    }

    @GetMapping("/dashboard/user/visits/delete")
    public String viewingConfirmViewDeleteVisit (@RequestParam Integer id, Model model){
        Order selectedVisit = orderService.findOrderById(id);
        model.addAttribute("selectedVisit", selectedVisit);
        model.addAttribute("id", id);

        return "dashboardPages/user/confirmationDeleteVisit";
    }

    @PostMapping("/dashboard/user/visits/delete")
    public String afterConfirmedBoxDeleteVisit (Integer id){
        orderService.deleteById(id);

        return "redirect:/dashboard/user/visits?visitDeleteSuccess";
    }
}

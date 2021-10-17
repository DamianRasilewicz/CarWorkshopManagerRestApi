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

    @GetMapping("/dashboard/user/visits/details")
    public String viewingSelectedVisit (@RequestParam Integer id, Model model){
        Order selectedVisit = orderService.findOrderById(id);
        model.addAttribute("selectedVisit", selectedVisit);

        return "dashboardPages/user/visitDetails";
    }

    @PostMapping("/dashboard/user/visits/details")
    public String changedSelectedVisit (@ModelAttribute("selectedVisitId") Integer selectedVisitId, @ModelAttribute("wroteComment") String wroteComment,
                                        @ModelAttribute("visitDateId") Integer visitDateId, @ModelAttribute("carId") Integer carId, RedirectAttributes redirectAttributes) {

        Order selectedVisit = orderService.findOrderById(selectedVisitId);

        selectedVisit.setCar(carService.findCarById(carId));
//        selectedVisit.setVisitDate(visitDateService.findVisitDateById(visitDateId));
        selectedVisit.setComment(wroteComment);
        orderService.save(selectedVisit);


        redirectAttributes.addAttribute("id", selectedVisit.getId());



        return  "redirect:/dashboard/user/visits/details?success";
    }

    @GetMapping("/dashboard/user/lastVisits/delete")
    public String viewingConfirmViewDeleteLastVisit (@RequestParam Integer id, Model model){
        Order selectedVisit = orderService.findOrderById(id);
        model.addAttribute("selectedVisit", selectedVisit);
        model.addAttribute("id", id);

        return "dashboardPages/user/confirmationDeleteLastVisit";
    }

    @PostMapping("/dashboard/user/lastVisits/delete")
    public String afterConfirmedBoxDeleteLastVisit (Integer id){
        orderService.deleteById(id);

        return "redirect:/dashboard/user/home?lastVisitDeleteSuccess";
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

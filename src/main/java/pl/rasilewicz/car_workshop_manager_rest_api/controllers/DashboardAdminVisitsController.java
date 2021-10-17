package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.services.CarServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.OrderServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.VisitDateServiceImpl;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardAdminVisitsController {

    private final OrderServiceImpl orderService;
    private final CarServiceImpl carService;
    private final VisitDateServiceImpl visitDateService;


    @GetMapping("/admins/{userId}/allVisits")
    public List <Order> adminAllVisits (@PathVariable Integer userId){


        return orderService.findAllOrders();
    }

    @GetMapping("/admins/{userId}/allVisits/{visitId}")
    public List<Object> viewingAdminSelectedVisit (@PathVariable Integer visitId, Model model){

        Order selectedVisit = orderService.findOrderById(visitId);

        List<String> statusList = Arrays.asList("Waiting for approval", "Pending", "In progress", "In progress - delayed",  "Done");

        List<Object> summary = Arrays.asList(selectedVisit, statusList);

        return summary;
    }

    @PutMapping("/admins/{userId}/allVisits/{visitId}")
    public String changeAdminSelectedVisit (@RequestBody Integer selectedVisitId, @RequestBody Double estimatedExecutionTime, @RequestBody Integer estimatedWorkCost,
                                            @RequestBody Integer workingHours, @RequestBody Double workCost, @RequestBody Double partsCost,
                                            @RequestBody Double finalCost, @RequestBody String moreInformation, @RequestBody String wroteComment,
                                            @RequestBody String status, RedirectAttributes redirectAttributes){

        Order selectedVisit = orderService.findOrderById(selectedVisitId);
        selectedVisit.setEstimatedExecutionTime(estimatedExecutionTime);
        selectedVisit.setEstimatedWorkCost(estimatedWorkCost);
        selectedVisit.setWorkingHours(workingHours);
        selectedVisit.setWorkCost(workCost);
        selectedVisit.setPartsCost(partsCost);
        selectedVisit.setFinalCost(finalCost);
        selectedVisit.setMoreInformation(moreInformation);
        selectedVisit.setComment(wroteComment);
        selectedVisit.setStatus(status);
        orderService.save(selectedVisit);


        redirectAttributes.addAttribute("id", selectedVisit.getId());



        return  "Success";
    }

    @DeleteMapping("/admins/{userId}/lastVisits/delete/{visitId}")
    public void DeleteLastVisit (@PathVariable Integer visitId){

        orderService.deleteById(visitId);

    }

    @GetMapping("/admins/")
    public String viewingConfirmViewDeleteVisit (@RequestParam Integer id, Model model){
        Order selectedVisit = orderService.findOrderById(id);
        model.addAttribute("selectedVisit", selectedVisit);
        model.addAttribute("id", id);

        return "dashboardPages/admin/confirmationDeleteVisit";
    }

    @PostMapping("/dashboard/admin/allVisits/delete")
    public String afterConfirmedBoxDeleteVisit (Integer id){
        orderService.deleteById(id);

        return "redirect:/dashboard/admin/allVisits?visitDeleteSuccess";
    }

    @GetMapping("/dashboard/admin/allVisits/allUndoneOrders")
    public String viewingAllUndoneOrders (Model model){
        List<Order> undoneOrderList = orderService.findAllUndoneOrders();
        model.addAttribute("undoneOrderList", undoneOrderList);

        return "dashboardPages/admin/allUndoneOrders";
    }

    @GetMapping("/dashboard/admin/allVisits/allUndoneOrders/details")
    public String viewingDetailsSelectedUndoneOrder (@RequestParam Integer id, Model model){
        Order selectedUndoneOrder = orderService.findOrderById(id);
        model.addAttribute("selectedUndoneOrder", selectedUndoneOrder);

        List<String> statusList = Arrays.asList("Waiting for approval", "Pending", "In progress", "In progress - delayed",  "Done");
        model.addAttribute("statusList", statusList);

        return "dashboardPages/admin/undoneOrderDetails";
    }

    @PostMapping("/dashboard/admin/allVisits/allUndoneOrders/details")
    public String changedUndoneSelectedOrder (@ModelAttribute("selectedVisitId") Integer selectedVisitId, @ModelAttribute("estimatedExecutionTime") Double estimatedExecutionTime, @ModelAttribute("estimatedWorkCost") Integer estimatedWorkCost,
                                              @ModelAttribute("workingHours") Integer workingHours, @ModelAttribute("workCost") Double workCost, @ModelAttribute("partsCost") Double partsCost,
                                              @ModelAttribute("finalCost") Double finalCost, @ModelAttribute("moreInformation") String moreInformation, @ModelAttribute("wroteComment") String wroteComment,
                                              @ModelAttribute("status") String status, RedirectAttributes redirectAttributes){

        Order selectedVisit = orderService.findOrderById(selectedVisitId);
        selectedVisit.setEstimatedExecutionTime(estimatedExecutionTime);
        selectedVisit.setEstimatedWorkCost(estimatedWorkCost);
        selectedVisit.setWorkingHours(workingHours);
        selectedVisit.setWorkCost(workCost);
        selectedVisit.setPartsCost(partsCost);
        selectedVisit.setFinalCost(finalCost);
        selectedVisit.setMoreInformation(moreInformation);
        selectedVisit.setComment(wroteComment);
        selectedVisit.setStatus(status);
        orderService.save(selectedVisit);


        redirectAttributes.addAttribute("id", selectedVisit.getId());



        return  "redirect:/dashboard/admin/allVisits/allUndoneOrders/details?success";
    }

    @GetMapping("/dashboard/admin/allVisits/allUndoneOrders/delete")
    public String deletingSelectedUndoneOrder (@RequestParam Integer id, Model model){
        Order selectedVisit = orderService.findOrderById(id);
        model.addAttribute("selectedVisit", selectedVisit);
        model.addAttribute("id", id);

        return "dashboardPages/admin/confirmationDeleteUndoneOrder";
    }

    @PostMapping("/dashboard/admin/allVisits/allUndoneOrders/delete")
    public String afterConfirmedBoxDeleteUndoneOrder (Integer id){
        orderService.deleteById(id);

        return "redirect:/dashboard/admin/allVisits/allUndoneOrders?undoneOrderDeleteSuccess";
    }
}

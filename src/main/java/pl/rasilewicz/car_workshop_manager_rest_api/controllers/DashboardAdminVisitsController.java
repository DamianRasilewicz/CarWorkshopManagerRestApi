package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.services.CarServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.OrderServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.VisitDateServiceImpl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public String viewingAdminSelectedVisit (@PathVariable Integer visitId){

        Order selectedVisit = orderService.findOrderById(visitId);

        List<String> statusList = Arrays.asList("Waiting for approval", "Pending", "In progress", "In progress - delayed",  "Done");

        Map<String, Object> summaryResult = new LinkedHashMap<>();

        summaryResult.put("selectedVisit", selectedVisit);
        summaryResult.put("statusList", statusList);

        Gson gson = new Gson();
        String json = gson.toJson(summaryResult);

        return json;
    }

    @PutMapping("/admins/{userId}/allVisits/{visitId}")
    public Order changeAdminSelectedVisit (@RequestBody Order order){

        return  orderService.editOrder(order);
    }

    @DeleteMapping("/admins/{userId}/lastVisits/{visitId}/delete")
    public void DeleteLastVisit (@PathVariable Integer visitId){

        orderService.deleteById(visitId);

    }


    @DeleteMapping("/admins/{userId}/allVisits/{visitId}/delete")
    public void DeleteVisit (@PathVariable Integer visitId){

        orderService.deleteById(visitId);

    }

    @GetMapping("/admins/{userId}/allVisits/allUndoneOrders")
    public List<Order> viewingAllUndoneOrders (){

        return orderService.findAllUndoneOrders();
    }

    @GetMapping("/admins/{userId}/allVisits/allUndoneOrders/{visitId}")
    public String viewingDetailsSelectedUndoneOrder (@PathVariable Integer visitId){
        Order selectedUndoneOrder = orderService.findOrderById(visitId);

        List<String> statusList = Arrays.asList("Waiting for approval", "Pending", "In progress", "In progress - delayed",  "Done");

        Map<String, Object> summaryResult = new LinkedHashMap<>();
        summaryResult.put("selectedUndoneOrder", selectedUndoneOrder);
        summaryResult.put("statusList", statusList);

        Gson gson = new Gson();
        String json = gson.toJson(summaryResult);

        return json;
    }

    @PutMapping("/dashboard/admin/allVisits/allUndoneOrders/details")
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

    @DeleteMapping("/admins/{userId}/allVisits/allUndoneOrders/{visitId}/delete")
    public void deletingSelectedUndoneOrder (@PathVariable Integer visitId){

        orderService.deleteById(visitId);

    }
}

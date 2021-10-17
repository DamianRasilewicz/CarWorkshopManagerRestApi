package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.services.CarServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.OrderServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.VisitDateServiceImpl;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class DashboardAdminVisitsController {

    private final OrderServiceImpl orderService;

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

    @PutMapping("/admins/{userId}/allVisits/allUndoneOrders/{visitId}")
    public Order changedUndoneSelectedOrder (@RequestBody Order order){

        return orderService.editOrder(order);
    }

    @DeleteMapping("/admins/{userId}/allVisits/allUndoneOrders/{visitId}/delete")
    public void deletingSelectedUndoneOrder (@PathVariable Integer visitId){

        orderService.deleteById(visitId);

    }
}

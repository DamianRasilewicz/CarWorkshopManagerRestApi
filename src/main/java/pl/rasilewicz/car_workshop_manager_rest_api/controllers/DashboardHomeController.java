package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.services.MechanicServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.OrderServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.VisitDateServiceImpl;

import java.time.LocalDate;
import java.util.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class DashboardHomeController {

    private final OrderServiceImpl orderService;
    private final VisitDateServiceImpl visitDateService;
    private final UserServiceImpl userService;
    private final MechanicServiceImpl mechanicService;


    @GetMapping("/users/{userId}/home")
    public String userHome(@PathVariable Integer userId) {
        List<Order> userLastOrderList = orderService.findLastOrdersByUserId(userId);

        List<String> monthsList = Arrays.asList("January", "February", "March", "April", "Mai", "June", "July",
                "August", "September", "October", "November", "December");

        Map<String, Integer> dataVisitsChart = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < monthsList.size(); i++) {
            dataVisitsChart.put(monthsList.get(i), visitDateService.findNumberOfVisitDatesByMonthByUserId(i + 1, userId));
        }

        Map<String, Object> summaryResult = new LinkedHashMap<>();
        summaryResult.put("userLastOrderList", userLastOrderList);
        summaryResult.put("dataVisitsChart", dataVisitsChart);
        summaryResult.put("monthsList", monthsList);

        Gson gson = new Gson();
        String json = gson.toJson(summaryResult);

        return json;
    }

    @GetMapping("/admins/{userId}/home")
    public String adminHome() {

        List<Order> usersLastOrderList = orderService.findLastUsersOrders();

        List<String> monthsList = Arrays.asList("January", "February", "March", "April", "Mai", "June", "July",
                "August", "September", "October", "November", "December");

        Map<String, Integer> dataVisitsChart = new LinkedHashMap<String, Integer>();
        Map<String, Integer> dataRevenuesChart = new LinkedHashMap<String, Integer>();
        Map<String, Integer> dataRegisteredUsersChart = new LinkedHashMap<String, Integer>();

        Integer presentYear = LocalDate.now().getYear();

        for (int i = 0; i < monthsList.size(); i++) {
            dataVisitsChart.put(monthsList.get(i), visitDateService.findNumberOfVisitDatesAllUsersOfYear(i + 1, presentYear));

            if (orderService.findMonthlyRevenue(i + 1, presentYear) == null){
                dataRevenuesChart.put(monthsList.get(i), 0);
            } else {
                dataRevenuesChart.put(monthsList.get(i), orderService.findMonthlyRevenue(i + 1, presentYear));
            }

            dataRegisteredUsersChart.put(monthsList.get(i), userService.findNumberOfMonthlyRegisteredUsers(i + 1, presentYear));
        }

        List<Order> threeUndoneOrderList = orderService.findThreeUndoneOrders();

        List<Order> undoneOrderList = orderService.findAllUndoneOrders();
        Integer numberOfUndoneOrders = undoneOrderList.size();

        Integer numberOfAllOrders = orderService.findNumberOfAllOrders();

        Integer numberOfAllUsers = userService.findNumberOfAllUsers();

        Integer totalRevenue = orderService.findTotalRevenue();

        Integer numberOfAllMechanics = mechanicService.findNumberOfMechanics();

        Map<String, Object> summaryResult= new LinkedHashMap<>();
        summaryResult.put("usersLastOrderList", usersLastOrderList);
        summaryResult.put("monthsList", monthsList);
        summaryResult.put("dataVisitsChart", dataVisitsChart);
        summaryResult.put("dataRevenuesChart", dataRevenuesChart);
        summaryResult.put("dataRegisteredUsersChart", dataRegisteredUsersChart);
        summaryResult.put("threeUndoneOrderList", threeUndoneOrderList);
        summaryResult.put("numberOfUndoneOrders", numberOfUndoneOrders);
        summaryResult.put("numberOfAllOrders", numberOfAllOrders);
        summaryResult.put("numberOfAllUsers", numberOfAllUsers);
        summaryResult.put("totalRevenue", totalRevenue);
        summaryResult.put("numberOfAllMechanics", numberOfAllMechanics);

        Gson gson = new Gson();
        String json = gson.toJson(summaryResult);

        return json;

    }
}

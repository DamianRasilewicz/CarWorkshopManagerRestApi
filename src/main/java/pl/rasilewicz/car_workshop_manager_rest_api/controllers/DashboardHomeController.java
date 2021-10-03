package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

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
    public List<Object> userHome(@PathVariable Integer userId) {
        List<Order> userLastOrderList = orderService.findLastOrdersByUserId(userId);

        List<String> monthsList = Arrays.asList("January", "February", "March", "April", "Mai", "June", "July",
                "August", "September", "October", "November", "December");

        Map<String, Integer> dataVisitsChart = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < monthsList.size(); i++) {
            dataVisitsChart.put(monthsList.get(i), visitDateService.findNumberOfVisitDatesByMonthByUserId(i + 1, userId));
        }

        List<Object> summary = Arrays.asList(userLastOrderList, dataVisitsChart);



        return summary;
    }

    @GetMapping("/dashboard/admin/home")
    public String adminIndex(Model model, HttpSession session) {
        List<Order> usersLastOrderList = orderService.findLastUsersOrders();
        model.addAttribute("usersOrderList", usersLastOrderList);

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

        model.addAttribute("dataVisitsChart", dataVisitsChart);
        model.addAttribute("dataRevenuesChart", dataRevenuesChart);
        model.addAttribute("dataRegisteredUsersChart", dataRegisteredUsersChart);
        model.addAttribute("presentYear", presentYear);

        List<Order> threeUndoneOrderList = orderService.findThreeUndoneOrders();
        model.addAttribute("threeUndoneOrderList", threeUndoneOrderList);

        List<Order> undoneOrderList = orderService.findAllUndoneOrders();
        Integer numberOfUndoneOrders = undoneOrderList.size();
        model.addAttribute("numberOfUndoneOrders", numberOfUndoneOrders);

        Integer numberOfAllOrders = orderService.findNumberOfAllOrders();
        model.addAttribute("numberOfAllOrders", numberOfAllOrders);

        Integer numberOfAllUsers = userService.findNumberOfAllUsers();
        model.addAttribute("numberOfAllUsers", numberOfAllUsers);

        Integer totalRevenue = orderService.findTotalRevenue();
        model.addAttribute("totalRevenue", totalRevenue);

        Integer numberOfAllMechanics = mechanicService.findNumberOfMechanics();
        model.addAttribute("numberOfAllMechanics", numberOfAllMechanics);

        return "dashboardPages/admin/dashboard";

    }
}

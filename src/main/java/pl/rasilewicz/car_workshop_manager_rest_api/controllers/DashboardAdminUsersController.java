package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardAdminUsersController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final OrderServiceImpl orderService;
    private final MailServiceImpl mailService;
    private final TemplateEngine templateEngine;
    private final VisitDateServiceImpl visitDateService;

    @GetMapping("/admins/{userId}/users")
    public List<User> userList (@PathVariable Integer userId) {
        User loggedInUser = userService.findUserById(userId);

        return userService.findAllUsersWithoutLogInUser(loggedInUser.getUserName());
    }

    @GetMapping("/admins/{userId}/users/{id}")
    public User userDetails (@PathVariable Integer id){

        return userService.findUserById(id);
    }

    @PutMapping("/admins/{userId}/users")
    public User editUserByAdmin (@RequestBody User user){

        return userService.editUser(user);
    }

    @DeleteMapping("/admins{userId}/users/{id}/delete")
    public void deleteUser (@PathVariable Integer id){

        userService.deleteById(id);
    }

    @GetMapping("/admins/{userId}/users/allVisitList")
    public List<Order> allVisitList(){


        return orderService.findAllOrders();
    }

    @GetMapping("/admins/{userId}/users/{id}/userVisits")
    public List<Order> selectedUserVisitList(@PathVariable Integer id){

        return orderService.findOrdersByUserId(id);
    }

    @GetMapping("/admins/{userId}/users/{id}/userVisits/{visitId}")
    public Order selectedUserVisitDetails(@PathVariable Integer id, @PathVariable Integer visitId){

        return orderService.findOrderById(visitId);
    }

    @PutMapping("/admins/{userId}/users/{id}/userVisits/{visitId}")
    public String selectedUserVisitEdited (@RequestBody Order editedOrder){

        orderService.editOrder(editedOrder);

        Context context = new Context();
        context.setVariable("selectedVisit", editedOrder);
        context.setVariable("visitDate", editedOrder.getVisitDate());
        context.setVariable("workshop", editedOrder.getVisitDate().getWorkshop());
        context.setVariable("car", editedOrder.getCar());
        context.setVariable("user", editedOrder.getUser());
        if (editedOrder.getStatus().equals("Done")){
            String body = templateEngine.process("/dashboardPages/admin/mailTemplateDone", context);
            mailService.sendEmail(editedOrder.getUser().getEmail(), "Car workshop manager. Your order's status has been changed!", body);
        }else {
            String body = templateEngine.process("/dashboardPages/admin/mailTemplateInProgress", context);
            mailService.sendEmail(editedOrder.getUser().getEmail(), "Car workshop manager. Your order's status has been changed!", body);
        }

        return  "success";
    }
}

package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
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

    @DeleteMapping("/admins{userId}/users{id}/delete")
    public void deleteUser (@PathVariable Integer id){

        userService.deleteById(id);
    }

    @GetMapping("/admins/{userId}/users/allVisitList")
    public List<Order> allVisitList(){


        return orderService.findAllOrders();
    }

    @GetMapping("/dashboard/admin/users/{id}/userVisitList")
    public List<Order> selectedUserVisitList(@PathVariable Integer id){

        return orderService.findOrdersByUserId(id);
    }

    @GetMapping("/dashboard/admin/users/userVisitDetails")
    public String selectedUserVisitDetails(@RequestParam Integer id, Model model){

        Order selectedOrderDetails = orderService.findOrderById(id);
        model.addAttribute("selectedOrderDetails", selectedOrderDetails);

        return "dashboardPages/admin/selectedUserVisitDetails";
    }

    @PostMapping("/dashboard/admin/users/userVisitDetails")
    public String selectedUserVisitEdited (@ModelAttribute("selectedVisitId") Integer selectedVisitId, @ModelAttribute("estimatedExecutionTime") Double estimatedExecutionTime, @ModelAttribute("estimatedWorkCost") Integer estimatedWorkCost,
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


        Context context = new Context();
        context.setVariable("selectedVisit", selectedVisit);
//        context.setVariable("visitDate", selectedVisit.getVisitDate());
//        context.setVariable("workshop", selectedVisit.getVisitDate().getWorkshop());
        context.setVariable("car", selectedVisit.getCar());
        context.setVariable("user", selectedVisit.getUser());
        if (selectedVisit.getStatus().equals("Done")){
            String body = templateEngine.process("/dashboardPages/admin/mailTemplateDone", context);
            mailService.sendEmail(selectedVisit.getUser().getEmail(), "Car workshop manager. Your order's status has been changed!", body);
        }else {
            String body = templateEngine.process("/dashboardPages/admin/mailTemplateInProgress", context);
            mailService.sendEmail(selectedVisit.getUser().getEmail(), "Car workshop manager. Your order's status has been changed!", body);
        }




        redirectAttributes.addAttribute("id", selectedVisit.getId());



        return  "redirect:/dashboard/admin/users/userVisitDetails?success";
    }
}

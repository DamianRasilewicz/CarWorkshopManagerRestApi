package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Role;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.services.MailServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.OrderServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.RoleServiceImpl;
import pl.rasilewicz.car_workshop_manager_rest_api.services.UserServiceImpl;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardAdminUsersController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final OrderServiceImpl orderService;
    private final MailServiceImpl mailService;
    private final TemplateEngine templateEngine;

    public DashboardAdminUsersController(UserServiceImpl userService, RoleServiceImpl roleService, OrderServiceImpl orderService,
                                         MailServiceImpl mailService, TemplateEngine templateEngine) {
        this.userService = userService;
        this.roleService = roleService;
        this.orderService = orderService;
        this.mailService = mailService;
        this.templateEngine = templateEngine;
    }

    @GetMapping("/dashboard/admin/users")
    public String userList (Model model, HttpSession session){
        List<User> userList = userService.findAllUsers((String)session.getAttribute("userName"));
        model.addAttribute("userList", userList);

        return "dashboardPages/admin/users";
    }

    @GetMapping("/dashboard/admin/users/edit")
    public String userDetails (@RequestParam Integer id, Model model){
        User user = userService.findUserById(id);
        model.addAttribute("user", user);

        List<Role> roleList = roleService.findAllRoles();
        model.addAttribute("roleList", roleList);

        List<Boolean> enabledList = Arrays.asList(true, false);
        model.addAttribute("enabledList", enabledList);

        return "dashboardPages/admin/userEdit";
    }

    @PostMapping("/dashboard/admin/users/edit")
    public String userDetailsChanged (@ModelAttribute("selectedUserId") Integer selectedUserId, @ModelAttribute("newRole") String newRole,
                                      @ModelAttribute("newEnabled") Boolean newEnabled, @RequestParam("role") Integer roleId, RedirectAttributes redirectAttributes){

        User editedUser = userService.findUserById(selectedUserId);

        Role selectedRole = roleService.findRoleById(roleId);

        editedUser.setRole(selectedRole);
        editedUser.setEnabled(newEnabled);
        userService.save(editedUser);

        redirectAttributes.addAttribute("id", selectedUserId);

        return "redirect:/dashboard/admin/users/edit?success";
    }

    @GetMapping("/dashboard/admin/users/delete")
    public String viewingAdminConfirmViewDeleteUser (@RequestParam Integer id, Model model){
        User selectedUser = userService.findUserById(id);
        model.addAttribute("selectedUser", selectedUser);
        model.addAttribute("id", id);

        return "dashboardPages/admin/confirmationDeleteUser";
    }

    @PostMapping("/dashboard/admin/users/delete")
    public String afterAdminConfirmedBoxDeleteUser (Integer id){
        userService.deleteById(id);

        return "redirect:/dashboard/admin/users?userDeleteSuccess";
    }

    @GetMapping("/dashboard/admin/users/userVisitList")
    public String userVisitList(Model model, HttpSession session){

        List<User> userList = userService.findAllUsers((String)session.getAttribute("userName"));
        model.addAttribute("userList", userList);

        return "dashboardPages/admin/userVisitListAllUsers";
    }

    @GetMapping("/dashboard/admin/users/userVisitList/show")
    public String selectedUserVisitList(@RequestParam Integer userId, Model model){

        List<Order> userOrderList = orderService.findOrdersByUserId(userId);
        model.addAttribute("userOrderList", userOrderList);

        model.addAttribute("userName", userService.findUserById(userId).getUserName());


        return "dashboardPages/admin/userVisitListSelectedUser";
    }

    @GetMapping("/dashboard/admin/users/userVisitDetails")
    public String selectedUserVisitDetails(@RequestParam Integer id, Model model){

        Order selectedOrderDetails = orderService.findOrderById(id);
        model.addAttribute("selectedOrderDetails", selectedOrderDetails);

        List<String> statusList = Arrays.asList("Waiting for approval", "Pending", "In progress", "In progress - delayed",  "Done");
        model.addAttribute("statusList", statusList);

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
        context.setVariable("visitDate", selectedVisit.getVisitDate());
        context.setVariable("workshop", selectedVisit.getVisitDate().getWorkshop());
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

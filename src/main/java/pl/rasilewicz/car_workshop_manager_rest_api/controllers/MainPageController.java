package pl.rasilewicz.car_workshop_manager_rest_api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.*;
import pl.rasilewicz.car_workshop_manager_rest_api.services.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

@RestController
@Controller
public class MainPageController {

    private final TaskServiceImpl taskService;
    private final WorkshopServiceImpl workshopService;
    private final VisitDateServiceImpl visitDateService;
    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;
    private final CarServiceImpl carService;
    private final RoleServiceImpl roleService;

    public MainPageController(TaskServiceImpl taskService, WorkshopServiceImpl workshopService,
                              VisitDateServiceImpl visitDateService, OrderServiceImpl orderService,
                              UserServiceImpl userService, CarServiceImpl carService, RoleServiceImpl roleService){
        this.taskService = taskService;
        this.workshopService = workshopService;
        this.visitDateService = visitDateService;
        this.orderService = orderService;
        this.userService = userService;
        this. carService = carService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String home(){

        return "mainPages/index";
    }

    @GetMapping("/appointmentDate")
    public List<Workshop> appointmentDate(){

        return workshopService.findAllWorkshops();
    }

    @PostMapping("/appointmentDate")
    public Map<String, Object> inputtedAppointmentDate (@RequestBody Integer workshopId,
                                          @RequestBody String selectedDate){
      LocalDate date = LocalDate.parse(selectedDate);
      List<VisitDate> visitDatesSelectedWorkshop = visitDateService.findVisitDateByDateAndWorkshopId(date, workshopId);

      List<String> availableVisitTimeList = new ArrayList<>();
      Collections.addAll(availableVisitTimeList, "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00",
              "14:00", "15:00", "16:00");

      List<String> noAvailableVisitTimeList = new ArrayList<>();

        for (VisitDate visitDate: visitDatesSelectedWorkshop ) {
            noAvailableVisitTimeList.add(visitDate.getTime());
        }

        availableVisitTimeList.removeAll(noAvailableVisitTimeList);

        Map<String, Object> summaryResult = new LinkedHashMap<>();
        summaryResult.put("workshopId", workshopId);
        summaryResult.put("selectedDate", selectedDate);
        summaryResult.put("availableVisitTimeList", availableVisitTimeList);

        return summaryResult;
    }

    @GetMapping("/appointmentDetails")
    public Map<String, Object> appointmentDetails(@RequestBody List<String> availableVisitTimeList,
                                     @RequestBody Integer selectedWorkshopId,
                                     @RequestBody String selectedDate, HttpSession session){
        User user = new User();

        Order order = new Order();

        Car car = new Car();

        List<Task> taskList = taskService.findAllTasks();
        session.setAttribute("allTasks", taskList);
        System.out.println(taskList.toString());

        Workshop selectedWorkshop = workshopService.findWorkshopById(selectedWorkshopId);

        List<String> carBrandsList = Arrays.asList("Abarth", "Acura","Alfa Romeo", "Alpine","Aston Martin", "Audi", "Bentley", "BMW" , "Brabus" ,"Bugatti", "Buick", "Cadillac",
                                                    "Chevrolet", "Chrysler", "Citroën", "Daihatsu", "Dodge", "Ferrari", "Fiat", "Ford", "FSO", "GAZ", "GMC",  "Honda",
                                                    "Hummer", "Hyundai", "Infiniti", "Isuzu", "Iveco", "Jaguar", "Jeep", "Kia", "Lada", "Lamborghini", "Lancia",
                                                    "Land Rover", "Lexus", "Lincoln", "Lotus", "Maserati", "Maybach", "Mazda", "Mercedes-Benz", "MG", "MINI", "Mitsubishi",
                                                    "Morgan", "Moskvich", "Nissan", "Oldsmobile", "Opel", "Peugeot", "Pontiac", "Porsche", "Renault", "Rolls-Royce","Rover",
                                                    "Saab","Skoda","Subaru","Suzuki","Tata","Tesla","Toyota","UAZ","Vauxhall","Volkswagen","Volvo","Lotus");

        List<String> engineTypesList = Arrays.asList("Benzine", "Diesel", "Hybrid", "Benzine + LPG", "CNG");

        Map<String, Object> summaryResult = new LinkedHashMap<>();
        summaryResult.put("user", user);
        summaryResult.put("order", order);
        summaryResult.put("car", car);
        summaryResult.put("allTasks", taskList);
        summaryResult.put("availableVisitTimeList", availableVisitTimeList);
        summaryResult.put("selectedWorkshopId", selectedWorkshopId);
        summaryResult.put("selectedDate", selectedDate);
        summaryResult.put("selectedWorkshop", selectedWorkshop);
        summaryResult.put("carBrandsList", carBrandsList);
        summaryResult.put("engineTypesList", engineTypesList);

        return summaryResult;
    }

    @PostMapping("/appointmentDetails")
    public VisitDate inputtedAppointmentDetails(@RequestBody User user, @RequestBody Order order, @RequestBody Car car, @RequestBody String selectedDate,
                                             @RequestBody String selectedTime, @RequestParam(value = "selectedTasks", required = false) Integer[] selectedTasks,
                                             @RequestBody List<String> availableVisitTimeList, @RequestBody Workshop selectedWorkshop, @RequestBody List<String> carBrandsList,
                                             @RequestBody List<String> engineTypesList, @RequestBody List<Task> allTasks){

        Double estimatedExecutionTime = 0.00;
        Integer estimatedCost = 0;

        List<Task> selectedTasksList = new ArrayList<>();

        if (selectedTasks != null) {

            for (Integer taskId : selectedTasks) {
                selectedTasksList.add(taskService.findTaskById(taskId));
            }
            for (Task task : selectedTasksList) {
                estimatedExecutionTime = estimatedExecutionTime + task.getEstimatedExecutionTime();
                estimatedCost = estimatedCost + task.getEstimatedCost();
            }
        }


        user.setRegistered(false);
        Role role = roleService.findRoleById(2);
        user.setRole(role);
        userService.save(user);

        car.setUser(user);
        carService.save(car);

        order.setTasks(selectedTasksList);
        order.setUser(user);
        order.setStatus("Pending approval");
        order.setEstimatedWorkCost(estimatedCost);
        order.setEstimatedExecutionTime(estimatedExecutionTime);
        orderService.save(order);

        LocalDate selectedDateParsed = LocalDate.parse(selectedDate);

        VisitDate selectedVisitDate = new VisitDate();
        selectedVisitDate.setDate(selectedDateParsed.toString());
        selectedVisitDate.setTime(selectedTime);
        selectedVisitDate.setWorkshop(selectedWorkshop);
        selectedVisitDate.setDay(selectedDateParsed.getDayOfMonth());
        selectedVisitDate.setMonth(selectedDateParsed.getMonthValue());
        selectedVisitDate.setYear(selectedDateParsed.getYear());
        selectedVisitDate.setUser(user);

        visitDateService.save(selectedVisitDate);

        return selectedVisitDate;
    }

    @GetMapping("/appointmentSuccess")
    public String successfullyMadeAppointment(){

        return "mainPages/appointmentSuccess";
    }


}

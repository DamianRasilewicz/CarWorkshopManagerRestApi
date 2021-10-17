package pl.rasilewicz.car_workshop_manager_rest_api.services;


import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;

import java.util.List;

public interface OrderService {

    void save (Order order);

    List<Order> findLastOrdersByUserId (Integer userId);

    List<Order> findOrdersByUserId(Integer userId);

    Order findOrderById(Integer orderId);

    void deleteById (Integer id);

    List<Order> findLastUsersOrders();

    List<Order> findAllOrders();

    List<Order> findThreeUndoneOrders();

    List<Order> findAllUndoneOrders();

    Integer findNumberOfAllOrders();

    Integer findTotalRevenue();

    Integer findMonthlyRevenue(Integer month, Integer year);

    Order editOrder(Order order);

}

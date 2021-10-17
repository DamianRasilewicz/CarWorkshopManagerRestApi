package pl.rasilewicz.car_workshop_manager_rest_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.User;
import pl.rasilewicz.car_workshop_manager_rest_api.repositories.OrderRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public List<Order> findLastOrdersByUserId(Integer userId) {
        return orderRepository.findLastOrdersByUserId(userId);
    }

    @Override
    public List<Order> findOrdersByUserId(Integer userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    @Override
    public Order findOrderById(Integer orderId) {
        return orderRepository.findOrderById(orderId);
    }

    @Override
    public void deleteById(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findLastUsersOrders() {
        return orderRepository.findLastUsersOrders();
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAllOrders();
    }

    @Override
    public List<Order> findThreeUndoneOrders() {
        return orderRepository.findThreeUndoneOrders();
    }

    @Override
    public List<Order> findAllUndoneOrders() {
        return orderRepository.findAllUndoneOrders();
    }

    @Override
    public Integer findNumberOfAllOrders() {
        return orderRepository.findNumberOfAllOrders();
    }

    @Override
    public Integer findTotalRevenue() {
        return orderRepository.findTotalRevenue();
    }

    @Override
    public Integer findMonthlyRevenue(Integer month, Integer year) {
        return orderRepository.findMonthlyRevenue(month, year);
    }

    @Override
    @Transactional
    public Order editOrder(Order order) {
        Order editOrder = orderRepository.findOrderById(order.getId());
        editOrder.setEstimatedExecutionTime(order.getEstimatedExecutionTime());
        editOrder.setEstimatedWorkCost(order.getEstimatedWorkCost());
        editOrder.setWorkingHours(order.getWorkingHours());
        editOrder.setWorkCost(order.getWorkCost());
        editOrder.setPartsCost(order.getPartsCost());
        editOrder.setFinalCost(order.getFinalCost());
        editOrder.setMoreInformation(order.getMoreInformation());
        editOrder.setComment(order.getComment());
        editOrder.setStatus(order.getStatus());

        return orderRepository.save(editOrder);
    }


}

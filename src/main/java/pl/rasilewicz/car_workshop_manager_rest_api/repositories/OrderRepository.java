package pl.rasilewicz.car_workshop_manager_rest_api.repositories;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.rasilewicz.car_workshop_manager_rest_api.entities.Order;
import java.util.List;

@Repository
@EntityScan(basePackages = "pl.rasilewicz.car_workshop_manager.entities")
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT * FROM car_workshop_manager.orders WHERE user_id = ? LIMIT 3", nativeQuery = true)
    List<Order> findLastOrdersByUserId (Integer userId);

    @Query(value = "SELECT * FROM car_workshop_manager.orders WHERE user_id = ?", nativeQuery = true)
    List<Order> findOrdersByUserId(Integer userId);

    @Query(value = "SELECT * FROM car_workshop_manager.orders" +
            " LEFT JOIN car_workshop_manager.visit_dates ON car_workshop_manager.orders.id = car_workshop_manager.visit_dates.order_id " +
            "WHERE id = ?", nativeQuery = true)
    Order findOrderById(Integer orderId);

    @Query(value = "SELECT * FROM car_workshop_manager.orders ORDER BY id DESC LIMIT 3", nativeQuery = true)
    List<Order> findLastUsersOrders();

    @Query(value = "SELECT * FROM car_workshop_manager.orders", nativeQuery = true)
    List<Order> findAllOrders();

    @Query(value = "SELECT * FROM car_workshop_manager.orders WHERE status != 'Done' LIMIT 3", nativeQuery = true)
    List<Order> findThreeUndoneOrders();

    @Query(value = "SELECT * FROM car_workshop_manager.orders WHERE status != 'Done'", nativeQuery = true)
    List<Order> findAllUndoneOrders();

    @Query(value = "SELECT  COUNT(id) FROM car_workshop_manager.orders",nativeQuery = true)
    Integer findNumberOfAllOrders();

    @Query(value = "SELECT  SUM(work_cost) FROM car_workshop_manager.orders",nativeQuery = true)
    Integer findTotalRevenue();

    @Query(value = "SELECT SUM(work_cost) FROM car_workshop_manager.orders LEFT JOIN visit_dates ON orders.id = visit_dates.order_id WHERE month = ?1 AND year = ?2",nativeQuery = true)
    Integer findMonthlyRevenue(Integer month, Integer year);

}

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

    @Query(value = "SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.car LEFT JOIN FETCH o.user LEFT JOIN FETCH o.visitDate WHERE o.user.id = ?1")
    List<Order> findOrdersByUserId(Integer userId);

    @Query(value = "SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.car LEFT JOIN FETCH o.user LEFT JOIN FETCH o.visitDate WHERE o.id = ?1")
    Order findOrderById(Integer orderId);

    @Query(value = "SELECT * FROM car_workshop_manager.orders ORDER BY id DESC LIMIT 3", nativeQuery = true)
    List<Order> findLastUsersOrders();

    @Query(value = "SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.car LEFT JOIN FETCH o.user LEFT JOIN FETCH o.visitDate")
    List<Order> findAllOrders();

    @Query(value = "SELECT * FROM car_workshop_manager.orders WHERE status != 'Done' LIMIT 3", nativeQuery = true)
    List<Order> findThreeUndoneOrders();

    @Query(value = "SELECT * FROM car_workshop_manager.orders WHERE status != 'Done'", nativeQuery = true)
    List<Order> findAllUndoneOrders();

    @Query(value = "SELECT  COUNT(id) FROM car_workshop_manager.orders",nativeQuery = true)
    Integer findNumberOfAllOrders();

    @Query(value = "SELECT  SUM(work_cost) FROM car_workshop_manager.orders",nativeQuery = true)
    Integer findTotalRevenue();

    @Query(value = "SELECT SUM(work_cost) FROM car_workshop_manager.orders LEFT JOIN visit_dates ON orders.visit_date_id = visit_dates.id WHERE month = ?1 AND year = ?2",nativeQuery = true)
    Integer findMonthlyRevenue(Integer month, Integer year);

}

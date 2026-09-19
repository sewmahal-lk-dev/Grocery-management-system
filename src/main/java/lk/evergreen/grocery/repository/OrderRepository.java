package lk.evergreen.grocery.repository;

import lk.evergreen.grocery.entity.Order;
import lk.evergreen.grocery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.user LEFT JOIN FETCH o.shippingAddress LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product ORDER BY o.orderDate DESC")
    List<Order> findAllWithUserAndItemsAndAddress();

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.user LEFT JOIN FETCH o.shippingAddress LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.status = :status ORDER BY o.orderDate DESC")
    List<Order> findByStatusWithUserAndItemsAndAddress(@Param("status") lk.evergreen.grocery.entity.OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate >= :startDate AND o.status != lk.evergreen.grocery.entity.OrderStatus.CANCELLED")
    BigDecimal calculateRevenueSince(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = lk.evergreen.grocery.entity.OrderStatus.CANCELLED")
    BigDecimal sumCancelledRevenue();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status IN (lk.evergreen.grocery.entity.OrderStatus.PENDING, lk.evergreen.grocery.entity.OrderStatus.CONFIRMED, lk.evergreen.grocery.entity.OrderStatus.SHIPPED)")
    long countActiveOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = lk.evergreen.grocery.entity.OrderStatus.SHIPPED")
    long countActiveDeliveries();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId")
    long countOrdersByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.user.id = :userId AND o.status != lk.evergreen.grocery.entity.OrderStatus.CANCELLED")
    BigDecimal sumSpentByUser(@Param("userId") Long userId);
}

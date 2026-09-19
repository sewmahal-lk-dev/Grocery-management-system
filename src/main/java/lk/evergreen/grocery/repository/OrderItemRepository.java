package lk.evergreen.grocery.repository;

import lk.evergreen.grocery.entity.Order;
import lk.evergreen.grocery.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);

    @Query("SELECT i FROM OrderItem i JOIN FETCH i.order o JOIN FETCH i.product p WHERE o.status = lk.evergreen.grocery.entity.OrderStatus.PENDING AND p.pricingMode = lk.evergreen.grocery.entity.PricingMode.WEIGHT_BASED_KG AND i.weighed = false")
    List<OrderItem> findPendingWeightVerificationItems();
}

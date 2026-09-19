package lk.evergreen.grocery.service.admin;

import lk.evergreen.grocery.dto.OrderResponseDTO;
import lk.evergreen.grocery.dto.PendingWeightItemResponseDTO;
import lk.evergreen.grocery.entity.*;
import lk.evergreen.grocery.repository.OrderItemRepository;
import lk.evergreen.grocery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminLogisticsService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AdminOrderService adminOrderService;

    @Transactional(readOnly = true)
    public List<PendingWeightItemResponseDTO> getPendingWeightItems() {
        List<OrderItem> items = orderItemRepository.findPendingWeightVerificationItems();
        return items.stream()
                .map(item -> PendingWeightItemResponseDTO.builder()
                        .itemId(item.getId())
                        .orderId(item.getOrder().getId())
                        .productName(item.getProduct().getName())
                        .targetQuantity(item.getQuantity())
                        .pricingMode(item.getProduct().getPricingMode().name())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void confirmWeight(Long itemId, BigDecimal actualWeight) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderItem not found"));

        if (item.getProduct().getPricingMode() != PricingMode.WEIGHT_BASED_KG) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item is not weight-based");
        }

        item.setActualWeight(actualWeight);
        item.setWeighed(true);
        // Captured price at the time of weighing = unit price * actual scale weight
        item.setPrice(item.getProduct().getPrice().multiply(actualWeight));
        orderItemRepository.save(item);

        // Recalculate whole order total amount
        Order order = item.getOrder();
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct().getPricingMode() == PricingMode.WEIGHT_BASED_KG) {
                if (oi.isWeighed()) {
                    total = total.add(oi.getProduct().getPrice().multiply(oi.getActualWeight()));
                } else {
                    // Fallback to original estimate until weighed
                    total = total.add(oi.getProduct().getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())));
                }
            } else {
                total = total.add(oi.getPrice());
            }
        }
        order.setTotalAmount(total);

        // Check if ALL weight-based items in this order are now verified
        boolean allWeighed = true;
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct().getPricingMode() == PricingMode.WEIGHT_BASED_KG && !oi.isWeighed()) {
                allWeighed = false;
                break;
            }
        }

        if (allWeighed) {
            order.setStatus(OrderStatus.CONFIRMED);
        }

        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getReadyForDispatchOrders() {
        List<Order> orders = orderRepository.findByStatusWithUserAndItemsAndAddress(OrderStatus.CONFIRMED);
        return orders.stream()
                .map(adminOrderService::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void dispatchOrder(Long orderId, String courierName) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is not fully verified and confirmed yet");
        }

        order.setCourierName(courierName);
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }
}

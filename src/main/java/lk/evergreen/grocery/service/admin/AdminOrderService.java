package lk.evergreen.grocery.service.admin;

import lk.evergreen.grocery.dto.OrderResponseDTO;
import lk.evergreen.grocery.entity.*;
import lk.evergreen.grocery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> listAllOrders() {
        List<Order> orders = orderRepository.findAllWithUserAndItemsAndAddress();
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateStatus(Long orderId, String statusStr) {
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order status: " + statusStr);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return convertToDTO(updated);
    }

    public OrderResponseDTO convertToDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        dto.setPaymentMethod(order.getPaymentMethod());

        // Map Customer
        User user = order.getUser();
        if (user != null) {
            dto.setCustomer(OrderResponseDTO.CustomerInfo.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .build());
        }

        // Map Shipping Address
        Address address = order.getShippingAddress();
        if (address != null) {
            dto.setShippingAddress(OrderResponseDTO.AddressInfo.builder()
                    .id(address.getId())
                    .label(address.getLabel())
                    .fullName(address.getFullName())
                    .streetAddress(address.getStreetAddress())
                    .phone(address.getPhone())
                    .build());
        }

        // Map Order Items
        if (order.getItems() != null) {
            List<OrderResponseDTO.ItemInfo> itemDTOs = order.getItems().stream()
                    .map(item -> {
                        Product p = item.getProduct();
                        return OrderResponseDTO.ItemInfo.builder()
                                .id(item.getId())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .productName(p != null ? p.getName() : "Unknown Product")
                                .productSku(p != null ? p.getSku() : null)
                                .productImageUrl(p != null ? p.getImageUrl() : null)
                                .build();
                    })
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }
}

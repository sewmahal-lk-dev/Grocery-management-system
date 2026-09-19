package lk.evergreen.grocery.service.admin;

import lk.evergreen.grocery.dto.AdminPaymentLedgerDTO;
import lk.evergreen.grocery.dto.TransactionDTO;
import lk.evergreen.grocery.entity.Order;
import lk.evergreen.grocery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminPaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public AdminPaymentLedgerDTO getPaymentLedger() {
        // Calculate start of the current month
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        BigDecimal monthlyRevenue = orderRepository.calculateRevenueSince(startOfMonth);
        BigDecimal refunds = orderRepository.sumCancelledRevenue();

        List<Order> orders = orderRepository.findAllWithUserAndItemsAndAddress();

        List<TransactionDTO> transactionDTOs = orders.stream()
                .map(order -> {
                    String method = order.getPaymentMethod();
                    if (method == null || method.trim().isEmpty()) {
                        method = "Stripe Card";
                    } else if (method.equalsIgnoreCase("stripe")) {
                        method = "Stripe Card";
                    } else if (method.equalsIgnoreCase("cash")) {
                        method = "Cash on Delivery";
                    } else {
                        method = method.substring(0, 1).toUpperCase() + method.substring(1);
                    }

                    return TransactionDTO.builder()
                            .id(order.getId())
                            .customerName(order.getUser().getName())
                            .amount(order.getTotalAmount())
                            .paymentMethod(method)
                            .orderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm")))
                            .status(order.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());

        return AdminPaymentLedgerDTO.builder()
                .monthlyRevenue(monthlyRevenue)
                .refunds(refunds)
                .transactions(transactionDTOs)
                .build();
    }
}

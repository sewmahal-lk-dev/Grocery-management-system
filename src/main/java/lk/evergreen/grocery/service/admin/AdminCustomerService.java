package lk.evergreen.grocery.service.admin;

import lk.evergreen.grocery.dto.CustomerResponseDTO;
import lk.evergreen.grocery.entity.User;
import lk.evergreen.grocery.entity.UserRole;
import lk.evergreen.grocery.repository.OrderRepository;
import lk.evergreen.grocery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> listAllCustomers() {
        List<User> shoppers = userRepository.findByRole(UserRole.USER);
        return shoppers.stream()
                .map(shopper -> {
                    long totalOrders = orderRepository.countOrdersByUser(shopper.getId());
                    BigDecimal totalSpent = orderRepository.sumSpentByUser(shopper.getId());

                    return CustomerResponseDTO.builder()
                            .id(shopper.getId())
                            .name(shopper.getName())
                            .email(shopper.getEmail())
                            .phone(shopper.getPhone())
                            .totalOrders(totalOrders)
                            .totalSpent(totalSpent)
                            .build();
                })
                .collect(Collectors.toList());
    }
}

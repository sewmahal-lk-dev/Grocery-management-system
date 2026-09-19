package lk.evergreen.grocery.service.admin;

import lk.evergreen.grocery.dto.DashboardStatsDTO;
import lk.evergreen.grocery.dto.OrderResponseDTO;
import lk.evergreen.grocery.entity.Product;
import lk.evergreen.grocery.repository.OrderRepository;
import lk.evergreen.grocery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminDashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminOrderService adminOrderService;

    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        LocalDateTime startOfToday = LocalDateTime.now().with(LocalTime.MIN);

        // 1. Calculate Daily Revenue
        BigDecimal dailyRevenue = orderRepository.calculateRevenueSince(startOfToday);

        // 2. Count Active Orders
        long activeOrders = orderRepository.countActiveOrders();

        // 3. Find All Low Stock products (below threshold of 20)
        List<Product> lowStockList = productRepository.findLowStockProducts(20);
        long lowStockCount = lowStockList.size();

        // 4. Count Active Deliveries (SHIPPED status)
        long activeDeliveries = orderRepository.countActiveDeliveries();

        // 5. Map top 5 recent orders
        List<OrderResponseDTO> allOrders = adminOrderService.listAllOrders();
        List<OrderResponseDTO> recentOrders = allOrders.stream()
                .limit(5)
                .collect(Collectors.toList());

        // 6. Map top 5 low stock products info
        List<DashboardStatsDTO.ProductInfo> recentLowStock = lowStockList.stream()
                .limit(5)
                .map(p -> DashboardStatsDTO.ProductInfo.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .sku(p.getSku())
                        .stockQuantity(p.getStockQuantity())
                        .pricingMode(p.getPricingMode().name())
                        .build())
                .collect(Collectors.toList());

        return DashboardStatsDTO.builder()
                .dailyRevenue(dailyRevenue)
                .activeOrdersCount(activeOrders)
                .lowStockCount(lowStockCount)
                .activeDeliveriesCount(activeDeliveries)
                .recentOrders(recentOrders)
                .lowStockProducts(recentLowStock)
                .build();
    }
}

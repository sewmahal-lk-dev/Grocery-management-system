package lk.evergreen.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {

    private BigDecimal dailyRevenue;
    private long activeOrdersCount;
    private long lowStockCount;
    private long activeDeliveriesCount;
    private List<OrderResponseDTO> recentOrders;
    private List<ProductInfo> lowStockProducts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductInfo {
        private Long id;
        private String name;
        private String sku;
        private Integer stockQuantity;
        private String pricingMode;
    }
}

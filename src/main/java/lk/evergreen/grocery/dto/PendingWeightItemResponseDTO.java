package lk.evergreen.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingWeightItemResponseDTO {
    private Long itemId;
    private Long orderId;
    private String productName;
    private Integer targetQuantity;
    private String pricingMode;
}

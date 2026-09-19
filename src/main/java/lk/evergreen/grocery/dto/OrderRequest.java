package lk.evergreen.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long userId;
    private Long addressId;
    private String paymentMethod; // e.g. "STRIPE" or "COD"
    private String deliveryType;
    private String scheduledDate;
    private String scheduledTimeSlot;
}

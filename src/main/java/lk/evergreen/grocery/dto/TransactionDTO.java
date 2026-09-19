package lk.evergreen.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private Long id;
    private String customerName;
    private BigDecimal amount;
    private String paymentMethod;
    private String orderDate;
    private String status;
}

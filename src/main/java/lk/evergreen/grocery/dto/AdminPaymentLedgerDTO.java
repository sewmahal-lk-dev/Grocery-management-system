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
public class AdminPaymentLedgerDTO {
    private BigDecimal monthlyRevenue;
    private BigDecimal refunds;
    private List<TransactionDTO> transactions;
}

package lk.evergreen.grocery.controller.admin;

import lk.evergreen.grocery.dto.OrderResponseDTO;
import lk.evergreen.grocery.dto.PendingWeightItemResponseDTO;
import lk.evergreen.grocery.service.admin.AdminLogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/logistics")
@CrossOrigin(origins = "*")
public class AdminLogisticsController {

    @Autowired
    private AdminLogisticsService adminLogisticsService;

    @GetMapping("/pending-weight")
    public ResponseEntity<List<PendingWeightItemResponseDTO>> getPendingWeightItems() {
        List<PendingWeightItemResponseDTO> list = adminLogisticsService.getPendingWeightItems();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/items/{itemId}/weigh")
    public ResponseEntity<Void> confirmWeight(
            @PathVariable("itemId") Long itemId,
            @RequestParam("weight") BigDecimal weight) {
        adminLogisticsService.confirmWeight(itemId, weight);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ready-dispatch")
    public ResponseEntity<List<OrderResponseDTO>> getReadyForDispatchOrders() {
        List<OrderResponseDTO> list = adminLogisticsService.getReadyForDispatchOrders();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/orders/{orderId}/dispatch")
    public ResponseEntity<Void> dispatchOrder(
            @PathVariable("orderId") Long orderId,
            @RequestParam("courier") String courierName) {
        adminLogisticsService.dispatchOrder(orderId, courierName);
        return ResponseEntity.ok().build();
    }
}

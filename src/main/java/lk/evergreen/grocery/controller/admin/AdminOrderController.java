package lk.evergreen.grocery.controller.admin;

import lk.evergreen.grocery.dto.OrderResponseDTO;
import lk.evergreen.grocery.service.admin.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listAll() {
        return ResponseEntity.ok(adminOrderService.listAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        OrderResponseDTO updated = adminOrderService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}

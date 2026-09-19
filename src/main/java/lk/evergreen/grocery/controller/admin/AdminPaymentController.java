package lk.evergreen.grocery.controller.admin;

import lk.evergreen.grocery.dto.AdminPaymentLedgerDTO;
import lk.evergreen.grocery.service.admin.AdminPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/payments")
@CrossOrigin(origins = "*")
public class AdminPaymentController {

    @Autowired
    private AdminPaymentService adminPaymentService;

    @GetMapping("/ledger")
    public ResponseEntity<AdminPaymentLedgerDTO> getPaymentLedger() {
        AdminPaymentLedgerDTO ledger = adminPaymentService.getPaymentLedger();
        return ResponseEntity.ok(ledger);
    }
}

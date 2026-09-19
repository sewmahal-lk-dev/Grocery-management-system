package lk.evergreen.grocery.controller.admin;

import lk.evergreen.grocery.dto.CustomerResponseDTO;
import lk.evergreen.grocery.service.admin.AdminCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers")
@CrossOrigin(origins = "*")
public class AdminCustomerController {

    @Autowired
    private AdminCustomerService adminCustomerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> listAll() {
        List<CustomerResponseDTO> list = adminCustomerService.listAllCustomers();
        return ResponseEntity.ok(list);
    }
}

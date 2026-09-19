package lk.evergreen.grocery.controller.admin;

import lk.evergreen.grocery.dto.ProductRequest;
import lk.evergreen.grocery.entity.Product;
import lk.evergreen.grocery.service.admin.AdminProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
public class AdminProductController {

    @Autowired
    private AdminProductService adminProductService;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequest body) {
        Product saved = adminProductService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductRequest body) {
        Product saved = adminProductService.update(id, body);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminProductService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Integer stock = body.get("stock");
        if (stock == null || stock < 0) {
            return ResponseEntity.badRequest().build();
        }
        Product updated = adminProductService.updateStock(id, stock);
        return ResponseEntity.ok(updated);
    }
}

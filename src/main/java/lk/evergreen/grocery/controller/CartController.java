package lk.evergreen.grocery.controller;

import lk.evergreen.grocery.dto.CartDTO;
import lk.evergreen.grocery.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartDTO.Request request) {
        try {
            cartService.addToCart(request);
            return ResponseEntity.ok(Map.of("message", "Item added to cart successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartDTO.Item>> getUserCart(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(cartService.getUserCart(userId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(@RequestBody CartDTO.Request request) {
        try {
            cartService.updateQuantity(request);
            return ResponseEntity.ok(Map.of("message", "Quantity updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            cartService.removeFromCart(userId, productId);
            return ResponseEntity.ok(Map.of("message", "Item removed from cart successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<?> getCartSummary(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(cartService.getCartSummary(userId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
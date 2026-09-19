package lk.evergreen.grocery.controller;

import lk.evergreen.grocery.dto.PaymentRequest;
import lk.evergreen.grocery.dto.PaymentResponse;
import lk.evergreen.grocery.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        try {
            PaymentIntent intent = paymentService.createPaymentIntent(
                    paymentRequest.getAmount(),
                    paymentRequest.getCurrency(),
                    paymentRequest.getOrderId()
            );
            return ResponseEntity.ok(new PaymentResponse(intent.getClientSecret()));
        } catch (StripeException e) {
            // Returning the actual error message for debugging
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage(),
                    "code", e.getCode() != null ? e.getCode() : "unknown"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error: " + e.getMessage()));
        }
    }
}

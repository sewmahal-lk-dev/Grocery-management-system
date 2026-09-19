package lk.evergreen.grocery.controller;

import lk.evergreen.grocery.entity.Address;
import lk.evergreen.grocery.entity.User;
import lk.evergreen.grocery.repository.AddressRepository;
import lk.evergreen.grocery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(addressRepository.findByUser(userOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAddress(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            Address address = new Address();
            address.setUser(userOpt.get());
            address.setLabel(payload.get("label").toString());
            address.setFullName(payload.get("fullName").toString());
            address.setStreetAddress(payload.get("streetAddress").toString());
            address.setPhone(payload.get("phone").toString());

            addressRepository.save(address);
            return ResponseEntity.ok(Map.of("message", "Address saved successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Optional<Address> addressOpt = addressRepository.findById(id);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            address.setLabel(payload.get("label").toString());
            address.setFullName(payload.get("fullName").toString());
            address.setStreetAddress(payload.get("streetAddress").toString());
            address.setPhone(payload.get("phone").toString());

            addressRepository.save(address);
            return ResponseEntity.ok(Map.of("message", "Address updated successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Address deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/set-default/{id}")
    public ResponseEntity<?> setDefaultAddress(@PathVariable Long id) {
        Optional<Address> addressOpt = addressRepository.findById(id);
        if (addressOpt.isPresent()) {
            Address targetAddress = addressOpt.get();
            User user = targetAddress.getUser();

            // Clear existing defaults for this user
            List<Address> allUserAddresses = addressRepository.findByUser(user);
            allUserAddresses.forEach(a -> a.setDefault(false));

            // Set new default
            targetAddress.setDefault(true);

            addressRepository.saveAll(allUserAddresses);
            return ResponseEntity.ok(Map.of("message", "Default address updated", "address", targetAddress));
        }
        return ResponseEntity.notFound().build();
    }
}

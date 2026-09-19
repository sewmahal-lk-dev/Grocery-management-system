package lk.evergreen.grocery.service;

import lk.evergreen.grocery.dto.CartDTO;
import lk.evergreen.grocery.entity.Cart;
import lk.evergreen.grocery.entity.Product;
import lk.evergreen.grocery.entity.User;
import lk.evergreen.grocery.repository.CartRepository;
import lk.evergreen.grocery.repository.ProductRepository;
import lk.evergreen.grocery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void addToCart(CartDTO.Request request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);
        Cart cartItem;

        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = new Cart();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
        }

        cartRepository.save(cartItem);
    }

    public List<CartDTO.Item> getUserCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUser(user);
        return cartItems.stream().map(this::convertToItemDTO).collect(Collectors.toList());
    }

    @Transactional
    public void updateQuantity(CartDTO.Request request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);
        if (existingCartItem.isPresent()) {
            Cart cartItem = existingCartItem.get();
            if (request.getQuantity() <= 0) {
                cartRepository.delete(cartItem);
            } else {
                cartItem.setQuantity(request.getQuantity());
                cartRepository.save(cartItem);
            }
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);
        if (existingCartItem.isPresent()) {
            cartRepository.delete(existingCartItem.get());
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    public CartDTO.Summary getCartSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> items = cartRepository.findByUser(user);
        List<CartDTO.Item> itemDTOs = items.stream().map(this::convertToItemDTO).collect(Collectors.toList());

        BigDecimal subtotal = BigDecimal.ZERO;
        int totalItems = 0;
        for (CartDTO.Item item : itemDTOs) {
            subtotal = subtotal.add(item.getSubtotal());
            totalItems += item.getQuantity();
        }

        BigDecimal deliveryFee = totalItems > 0 ? new BigDecimal("250.00") : BigDecimal.ZERO;
        BigDecimal grandTotal = subtotal.add(deliveryFee);

        return CartDTO.Summary.builder()
                .items(itemDTOs)
                .itemCount(totalItems)
                .subtotal(subtotal)
                .deliveryFee(deliveryFee)
                .grandTotal(grandTotal)
                .build();
    }

    private CartDTO.Item convertToItemDTO(Cart item) {
        BigDecimal price = item.getProduct().getPrice();
        BigDecimal quantity = new BigDecimal(item.getQuantity());
        BigDecimal subtotal = price.multiply(quantity);

        CartDTO.ProductInfo productInfo = CartDTO.ProductInfo.builder()
                .id(item.getProduct().getId())
                .name(item.getProduct().getName())
                .imageUrl(item.getProduct().getImageUrl())
                .price(price)
                .build();

        return CartDTO.Item.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .product(productInfo)
                .build();
    }
}
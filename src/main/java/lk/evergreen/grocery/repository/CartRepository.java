package lk.evergreen.grocery.repository;

import lk.evergreen.grocery.entity.Cart;
import lk.evergreen.grocery.entity.User;
import lk.evergreen.grocery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Optional<Cart> findByUserAndProduct(User user, Product product);
}
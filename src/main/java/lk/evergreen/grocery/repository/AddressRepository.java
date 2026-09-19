package lk.evergreen.grocery.repository;

import lk.evergreen.grocery.entity.Address;
import lk.evergreen.grocery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    Optional<Address> findByUserAndIsDefault(User user, boolean isDefault);
}

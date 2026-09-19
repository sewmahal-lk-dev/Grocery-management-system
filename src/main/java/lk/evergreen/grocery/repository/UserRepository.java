package lk.evergreen.grocery.repository;

import lk.evergreen.grocery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    java.util.List<User> findByRole(lk.evergreen.grocery.entity.UserRole role);
}

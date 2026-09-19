package lk.evergreen.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Registered account for the store.
 * <p>
 * <b>Role ({@link #role})</b> decides what the UI (and later Spring Security) may allow:
 * <ul>
 *   <li>{@link UserRole#USER} — shopper: storefront, cart, own profile.</li>
 *   <li>{@link UserRole#ADMIN} — operator: admin pages, catalog/orders management.</li>
 * </ul>
 * Public registration always saves as {@link UserRole#USER}; {@link UserRole#ADMIN} is assigned
 * manually (e.g. SQL update) until a secure admin-invite flow exists.
 */
@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    private String university;

    @Column(columnDefinition = "TEXT")
    private String bio;

    /**
     * Distinguishes customer vs admin after login; exposed in JSON (password is cleared server-side).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.USER;
}

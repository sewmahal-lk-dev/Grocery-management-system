package lk.evergreen.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label; // e.g., Home, Office
    private String fullName;

    @Column(columnDefinition = "TEXT")
    private String streetAddress;

    private String phone;

    private boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

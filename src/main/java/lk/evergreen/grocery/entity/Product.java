package lk.evergreen.grocery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    /** Public selling price in LKR; meaning depends on {@link #pricingMode}. */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(unique = true, length = 64)
    private String sku;

    @Column(nullable = false)
    private Integer stockQuantity = 0;

    @Column(nullable = false)
    private Boolean active = true;

    /** Optional absolute or CDN URL for list/detail images (upload flow stores path/URL here). */
    @Column(length = 2048)
    private String imageUrl;

    private LocalDate manufacturingDate;

    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private lk.evergreen.grocery.entity.PricingMode pricingMode = lk.evergreen.grocery.entity.PricingMode.WEIGHT_BASED_KG;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private lk.evergreen.grocery.entity.Category category;
}

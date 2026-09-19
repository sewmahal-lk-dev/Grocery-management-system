package lk.evergreen.grocery.dto;

import lk.evergreen.grocery.entity.PricingMode;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private Integer stockQuantity;
    private Boolean active;
    private String imageUrl;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private PricingMode pricingMode;
    private Long categoryId;
}

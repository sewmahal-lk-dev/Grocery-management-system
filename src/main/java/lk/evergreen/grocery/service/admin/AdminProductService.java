package lk.evergreen.grocery.service.admin;

import lk.evergreen.grocery.dto.ProductRequest;
import lk.evergreen.grocery.entity.Category;
import lk.evergreen.grocery.entity.PricingMode;
import lk.evergreen.grocery.entity.Product;
import lk.evergreen.grocery.repository.CategoryRepository;
import lk.evergreen.grocery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public Product create(ProductRequest req) {
        validateRequest(req);
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid categoryId"));
        Product p = mapNewProduct(req, category);
        return productRepository.save(p);
    }

    @Transactional
    public Product update(Long id, ProductRequest req) {
        validateRequest(req);
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid categoryId"));
        applyRequest(p, req, category);
        return productRepository.save(p);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    private void validateRequest(ProductRequest req) {
        if (req.getName() == null || req.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        if (req.getPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price is required");
        }
        if (req.getCategoryId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoryId is required");
        }
    }

    private Product mapNewProduct(ProductRequest req, Category category) {
        Product p = new Product();
        applyRequest(p, req, category);
        return p;
    }

    private void applyRequest(Product p, ProductRequest req, Category category) {
        p.setName(req.getName().trim());
        p.setDescription(req.getDescription() != null ? req.getDescription().trim() : null);
        p.setPrice(req.getPrice());
        p.setSku(emptyToNull(req.getSku()));
        p.setStockQuantity(req.getStockQuantity() != null ? req.getStockQuantity() : 0);
        p.setActive(req.getActive() != null ? req.getActive() : true);
        p.setImageUrl(emptyToNull(req.getImageUrl()));
        p.setManufacturingDate(req.getManufacturingDate());
        p.setExpiryDate(req.getExpiryDate());
        p.setPricingMode(req.getPricingMode() != null ? req.getPricingMode() : PricingMode.WEIGHT_BASED_KG);
        p.setCategory(category);
    }

    private static String emptyToNull(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        return s.trim();
    }

    @Transactional
    public Product updateStock(Long id, Integer stock) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        p.setStockQuantity(stock);
        return productRepository.save(p);
    }
}

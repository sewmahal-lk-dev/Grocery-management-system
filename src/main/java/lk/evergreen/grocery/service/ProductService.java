package lk.evergreen.grocery.service;

import lk.evergreen.grocery.entity.Product;
import lk.evergreen.grocery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public List<Product> listAll() {
        return productRepository.findAllWithCategoryOrdered();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findByIdWithCategory(id);
    }

    public List<Product> search(String query) {
        return productRepository.searchByName(query);
    }

}


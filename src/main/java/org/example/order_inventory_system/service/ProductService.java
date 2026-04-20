package org.example.order_inventory_system.service;

import org.example.order_inventory_system.exception.ProductNotFoundException;
import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Integer id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.deleteById(id);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name);
    }
}
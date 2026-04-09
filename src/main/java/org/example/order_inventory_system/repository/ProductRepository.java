package org.example.order_inventory_system.repository;


import org.example.order_inventory_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductNameContainingIgnoreCase(String name);
    List<Product> findByBrandIgnoreCase(String brand);
}
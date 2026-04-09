package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    // GET all products
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    // GET single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    // GET products by name search
    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    // POST create new product
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        product.setProductId(null); // ensure insert, not update
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    // PUT update existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Integer id,
                                          @Valid @RequestBody Product product) {
        productService.findById(id); // throws 500 if not found
        product.setProductId(id);
        return ResponseEntity.ok(productService.save(product));
    }

    // DELETE product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
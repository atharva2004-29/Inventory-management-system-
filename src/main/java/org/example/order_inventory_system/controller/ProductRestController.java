package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.service.InventoryService;
import org.example.order_inventory_system.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;
    private final InventoryService inventoryService;

    public ProductRestController(ProductService productService, InventoryService inventoryService) {
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    // GET inventory for a product (which stores stock it and how many)
    @GetMapping("/{id}/inventory")
    public ResponseEntity<List<Inventory>> getInventory(@PathVariable Integer id) {
        productService.findById(id);
        return ResponseEntity.ok(inventoryService.findAllByProductId(id));
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        product.setProductId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Integer id,
                                          @Valid @RequestBody Product product) {
        productService.findById(id);
        product.setProductId(id);
        return ResponseEntity.ok(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
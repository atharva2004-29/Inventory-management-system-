package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryRestController {

    private final InventoryService inventoryService;

    public InventoryRestController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // GET all inventory
    @GetMapping
    public ResponseEntity<List<Inventory>> getAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    // GET single inventory by ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }

    // GET by product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Inventory>> getByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(inventoryService.findAllByProductId(productId));
    }

    // GET by store ID
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Inventory>> getByStoreId(@PathVariable Integer storeId) {
        return ResponseEntity.ok(inventoryService.findByStoreId(storeId));
    }

    // GET low stock
    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStock(@RequestParam Integer threshold) {
        return ResponseEntity.ok(inventoryService.findLowStock(threshold));
    }

    // POST create new inventory
    @PostMapping
    public ResponseEntity<Inventory> create(@Valid @RequestBody Inventory inventory) {
        inventory.setInventoryId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.save(inventory));
    }

    // PUT update full inventory record
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> update(@PathVariable Integer id,
                                            @Valid @RequestBody Inventory inventory) {
        inventoryService.findById(id);
        inventory.setInventoryId(id);
        return ResponseEntity.ok(inventoryService.save(inventory));
    }

    // PATCH update stock quantity only
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Inventory> updateQuantity(@PathVariable Integer id,
                                                    @RequestParam Integer productInventory) {
        return ResponseEntity.ok(inventoryService.updateQuantity(id, productInventory));
    }

    // POST reserve stock
    @PostMapping("/reserve")
    public ResponseEntity<String> reserveStock(@RequestParam Integer productId,
                                               @RequestParam Integer quantity) {
        boolean success = inventoryService.reserveStock(productId, quantity);
        return success
                ? ResponseEntity.ok("Stock reserved successfully")
                : ResponseEntity.badRequest().body("Insufficient stock");
    }

    // POST release stock
    @PostMapping("/release")
    public ResponseEntity<String> releaseStock(@RequestParam Integer productId,
                                               @RequestParam Integer quantity) {
        inventoryService.releaseStock(productId, quantity);
        return ResponseEntity.ok("Stock released successfully");
    }

    // DELETE inventory
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        inventoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET count
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok((long) inventoryService.findAll().size());
    }

    // GET export (mock)
    @GetMapping("/export")
    public ResponseEntity<String> exportCsv() {
        return ResponseEntity.ok("InventoryID,Store,Product,Quantity\n1,Main Store,Laptop,50\n2,Sub Store,Mouse,100");
    }
}
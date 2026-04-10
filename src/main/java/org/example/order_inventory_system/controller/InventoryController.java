package org.example.order_inventory_system.controller;



import jakarta.validation.Valid;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public Inventory addInventory(@Valid @RequestBody Inventory inventory) {
        return inventoryService.addInventory(inventory);
    }

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/product/{productId}")
    public Inventory getByProductId(@PathVariable Integer productId) {
        return inventoryService.getByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    @PutMapping("/product/{productId}")
    public Inventory updateInventory(@PathVariable Integer productId,
                                     @RequestParam Integer quantity) {
        return inventoryService.updateInventory(productId, quantity);
    }

    @DeleteMapping("/{id}")
    public String deleteInventory(@PathVariable Integer id) {
        inventoryService.deleteInventory(id);
        return "Inventory deleted successfully";
    }


    @GetMapping("/low-stock")
    public List<Inventory> getLowStock(@RequestParam Integer threshold) {
        return inventoryService.getLowStockItems(threshold);
    }


    @PostMapping("/reserve")
    public String reserveStock(@RequestParam Integer productId,
                               @RequestParam Integer quantity) {
        boolean success = inventoryService.reserveStock(productId, quantity);
        return success ? "Stock reserved" : "Insufficient stock";
    }

    @PostMapping("/release")
    public String releaseStock(@RequestParam Integer productId,
                               @RequestParam Integer quantity) {
        inventoryService.releaseStock(productId, quantity);
        return "Stock released";
    }
}
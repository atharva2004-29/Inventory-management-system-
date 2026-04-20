package org.example.order_inventory_system.service;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Inventory findById(Integer id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found: " + id));
    }

    public List<Inventory> findAllByProductId(Integer productId) {
        return inventoryRepository.findAllByProduct_ProductId(productId);
    }

    public Optional<Inventory> findByProductId(Integer productId) {
        return inventoryRepository.findByProduct_ProductId(productId);
    }

    public List<Inventory> findByStoreId(Integer storeId) {
        return inventoryRepository.findByStore_StoreId(storeId);
    }

    public List<Inventory> findLowStock(Integer threshold) {
        return inventoryRepository.findByProductInventoryLessThan(threshold);
    }

    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory updateQuantity(Integer id, Integer productInventory) {
        Inventory inventory = findById(id);
        inventory.setProductInventory(productInventory);
        return inventoryRepository.save(inventory);
    }

    public void deleteById(Integer id) {
        inventoryRepository.deleteById(id);
    }

    public boolean reserveStock(Integer productId, Integer quantity) {
        Optional<Inventory> opt = inventoryRepository.findByProduct_ProductId(productId);
        if (opt.isPresent() && opt.get().getProductInventory() >= quantity) {
            opt.get().setProductInventory(opt.get().getProductInventory() - quantity);
            inventoryRepository.save(opt.get());
            return true;
        }
        return false;
    }

    public void releaseStock(Integer productId, Integer quantity) {
        Optional<Inventory> opt = inventoryRepository.findByProduct_ProductId(productId);
        opt.ifPresent(inv -> {
            inv.setProductInventory(inv.getProductInventory() + quantity);
            inventoryRepository.save(inv);
        });
    }
}
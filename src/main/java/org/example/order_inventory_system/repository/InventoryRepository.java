package org.example.order_inventory_system.repository;

import org.example.order_inventory_system.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByProduct_ProductId(Integer productId);
    List<Inventory> findByStore_StoreId(Integer storeId);
    List<Inventory> findByProductInventoryLessThan(Integer threshold);
    List<Inventory> findAllByProduct_ProductId(Integer productId);
}
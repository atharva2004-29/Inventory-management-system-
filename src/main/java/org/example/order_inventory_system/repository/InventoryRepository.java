package org.example.order_inventory_system.repository;


import org.example.order_inventory_system.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByProductId(Integer productId);

    List<Inventory> findByQuantityLessThan(Integer quantity);
}

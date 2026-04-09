package org.example.order_inventory_system.repository;

import org.example.order_inventory_system.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findByStoreNameContainingIgnoreCase(String name);
}

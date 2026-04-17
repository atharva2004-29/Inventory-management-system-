package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.repository.StoreRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    public Store findById(Integer id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found: " + id));
    }

    public Store save(Store store) {
        return storeRepository.save(store);
    }

    public void deleteById(Integer id) {
        storeRepository.deleteById(id);
    }

    public List<Store> searchByName(String name) {
        return storeRepository.findByStoreNameContainingIgnoreCase(name);
    }
}

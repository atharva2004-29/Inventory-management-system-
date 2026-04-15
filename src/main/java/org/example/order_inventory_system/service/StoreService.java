package org.example.order_inventory_system.service;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.exception.StoreNotFoundException;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.repository.StoreRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    public Store findById(Integer id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException(id));
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

package org.example.order_inventory_system.config;

import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.repository.StoreRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter implements Converter<String, Store> {

    private final StoreRepository storeRepository;

    public StoreConverter(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Store convert(String id) {
        if (id == null || id.isBlank()) return null;
        return storeRepository.findById(Integer.parseInt(id)).orElse(null);
    }
}
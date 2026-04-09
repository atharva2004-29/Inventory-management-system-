package org.example.order_inventory_system.config;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.repository.StoreRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreConverter implements Converter<String, Store> {

    private final StoreRepository storeRepository;

    @Override
    public Store convert(String id) {
        if (id == null || id.isBlank()) return null;
        return storeRepository.findById(Integer.parseInt(id)).orElse(null);
    }
}
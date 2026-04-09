package org.example.order_inventory_system.config;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.repository.OrderRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConverter implements Converter<String, Order> {

    private final OrderRepository orderRepository;

    @Override
    public Order convert(String id) {
        if (id == null || id.isBlank()) return null;
        return orderRepository.findById(Integer.parseInt(id)).orElse(null);
    }
}
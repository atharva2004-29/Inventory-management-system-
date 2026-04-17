package org.example.order_inventory_system.config;

import org.example.order_inventory_system.exception.OrderNotFoundException;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.repository.OrderRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements Converter<String, Order> {

    private final OrderRepository orderRepository;

    public OrderConverter(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order convert(String id) {
        if (id == null || id.isBlank()) return null;
        try {
            Integer orderId = Integer.parseInt(id);
            return orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
        } catch (NumberFormatException e) {
            throw new OrderNotFoundException("Invalid order ID format: " + id);
        }
    }
}
package org.example.order_inventory_system.service;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.OrderItem;
import org.example.order_inventory_system.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> findByOrderId(Integer orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }

    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public void deleteById(Integer id) {
        orderItemRepository.deleteById(id);
    }
}
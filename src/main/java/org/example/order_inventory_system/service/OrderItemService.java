package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.OrderItem;
import org.example.order_inventory_system.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

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
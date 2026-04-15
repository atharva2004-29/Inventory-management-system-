package org.example.order_inventory_system.repository;

import org.example.order_inventory_system.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder_OrderId(Integer orderId);
    Optional<OrderItem> findByOrder_OrderIdAndProduct_ProductId(Integer orderId, Integer productId);
}
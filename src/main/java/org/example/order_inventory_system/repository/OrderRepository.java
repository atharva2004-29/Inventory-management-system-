package org.example.order_inventory_system.repository;

import org.example.order_inventory_system.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomer_CustomerId(Integer customerId);
    List<Order> findByOrderStatus(String status);

}
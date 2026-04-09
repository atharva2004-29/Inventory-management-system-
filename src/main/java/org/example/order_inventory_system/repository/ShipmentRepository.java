package org.example.order_inventory_system.repository;

import org.example.order_inventory_system.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    List<Shipment> findByShipmentStatus(String status);
    List<Shipment> findByCustomer_CustomerId(Integer customerId);
    List<Shipment> findByOrder_OrderId(Integer orderId);
}
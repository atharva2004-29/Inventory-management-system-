package org.example.order_inventory_system.service;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public List<Shipment> findAll() {
        return shipmentRepository.findAll();
    }

    public Shipment findById(Integer id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + id));
    }

    public Shipment save(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public void deleteById(Integer id) {
        shipmentRepository.deleteById(id);
    }

    public List<Shipment> findByStatus(String status) {
        return shipmentRepository.findByShipmentStatus(status);
    }

    public List<Shipment> findByCustomerId(Integer customerId) {
        return shipmentRepository.findByCustomer_CustomerId(customerId);
    }

    public List<Shipment> findByOrderId(Integer orderId) {
        return shipmentRepository.findByOrder_OrderId(orderId);
    }
}
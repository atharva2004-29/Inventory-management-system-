//package org.example.order_inventory_system.service;
//
//import org.example.order_inventory_system.model.Shipment;
//import org.example.order_inventory_system.repository.ShipmentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class ShipmentService {
//
//    @Autowired
//    private ShipmentRepository shipmentRepository;
//
//    public Shipment createShipment(Shipment shipment) {
//        return shipmentRepository.save(shipment);
//    }
//
//    public List<Shipment> getAllShipments() {
//        return shipmentRepository.findAll();
//    }
//
//    public Optional<Shipment> getShipmentById(Integer id) {
//        return shipmentRepository.findById(id);
//    }
//
//    public List<Shipment> getShipmentByOrderId(Integer orderId) {
//        return shipmentRepository.findByOrderId(orderId);
//    }
//
//    public Shipment updateShipment(Integer id, Shipment updatedShipment) {
//        return shipmentRepository.findById(id).map(shipment -> {
//            shipment.setOrderId(updatedShipment.getOrderId());
//            shipment.setShippingAddress(updatedShipment.getShippingAddress());
//            shipment.setStatus(updatedShipment.getStatus());
//            shipment.setTrackingNumber(updatedShipment.getTrackingNumber());
//            shipment.setCarrier(updatedShipment.getCarrier());
//            shipment.setEstimatedDeliveryDate(updatedShipment.getEstimatedDeliveryDate());
//            shipment.setActualDeliveryDate(updatedShipment.getActualDeliveryDate());
//            return shipmentRepository.save(shipment);
//        }).orElseThrow(() -> new RuntimeException("Shipment not found"));
//    }
//
//    public void deleteShipment(Integer id) {
//        shipmentRepository.deleteById(id);
//    }
//}
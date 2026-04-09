///*
//package org.example.order_inventory_system.controller;
//
//import jakarta.validation.Valid;
//import org.example.order_inventory_system.model.Shipment;
//import org.example.order_inventory_system.service.ShipmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/shipments")
//public class ShipmentController {
//
//    @Autowired
//    private ShipmentService shipmentService;
//
//    @PostMapping
//    public Shipment createShipment(@Valid @RequestBody Shipment shipment) {
//        return shipmentService.createShipment(shipment);
//    }
//
//    @GetMapping
//    public List<Shipment> getAllShipments() {
//        return shipmentService.getAllShipments();
//    }
//
//    @GetMapping("/{id}")
//    public Shipment getShipmentById(@PathVariable Integer id) {
//        return shipmentService.getShipmentById(id)
//                .orElseThrow(() -> new RuntimeException("Shipment not found"));
//    }
//
//    @GetMapping("/order/{orderId}")
//    public List<Shipment> getShipmentByOrderId(@PathVariable Integer orderId) {
//        return shipmentService.getShipmentByOrderId(orderId);
//    }
//
//    @PutMapping("/{id}")
//    public Shipment updateShipment(@PathVariable Integer id,
//                                   @RequestBody Shipment shipment) {
//        return shipmentService.updateShipment(id, shipment);
//    }
//
//}*/

package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentRestController {

    private final ShipmentService shipmentService;

    // GET all shipments
    @GetMapping
    public ResponseEntity<List<Shipment>> getAll() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    // GET single shipment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(shipmentService.findById(id));
    }

    // GET by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Shipment>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(shipmentService.findByStatus(status));
    }

    // GET by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Shipment>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(shipmentService.findByCustomerId(customerId));
    }

    // GET by order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Shipment>> getByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(shipmentService.findByOrderId(orderId));
    }

    // POST create new shipment
    @PostMapping
    public ResponseEntity<Shipment> create(@Valid @RequestBody Shipment shipment) {
        shipment.setShipmentId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.save(shipment));
    }

    // PUT update existing shipment
    @PutMapping("/{id}")
    public ResponseEntity<Shipment> update(@PathVariable Integer id,
                                           @Valid @RequestBody Shipment shipment) {
        shipmentService.findById(id); // throws if not found
        shipment.setShipmentId(id);
        return ResponseEntity.ok(shipmentService.save(shipment));
    }

    // DELETE shipment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        shipmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
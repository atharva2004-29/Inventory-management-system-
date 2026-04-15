package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.findByStatus(status));
    }

    // GET orders by customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.findByCustomerId(customerId));
    }

    // GET orders by store
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Order>> getByStore(@PathVariable Integer storeId) {
        return ResponseEntity.ok(orderService.findByStoreId(storeId));
    }

    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody Order order) {
        order.setOrderId(null);
        if (order.getOrderTms() == null) {
            order.setOrderTms(java.time.LocalDateTime.now()); // auto-set if not provided
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Integer id,
                                        @Valid @RequestBody Order order) {
        orderService.findById(id);
        order.setOrderId(id);
        return ResponseEntity.ok(orderService.save(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
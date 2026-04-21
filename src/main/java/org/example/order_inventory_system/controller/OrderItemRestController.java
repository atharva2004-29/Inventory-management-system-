package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.OrderItem;
import org.example.order_inventory_system.service.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemRestController {

    private final OrderItemService orderItemService;

    // GET all items for a specific order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderItemService.findByOrderId(orderId));
    }

    // POST add a new item to an order
    @PostMapping
    public ResponseEntity<OrderItem> create(@Valid @RequestBody OrderItem orderItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemService.save(orderItem));
    }

    // DELETE an item from an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        orderItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
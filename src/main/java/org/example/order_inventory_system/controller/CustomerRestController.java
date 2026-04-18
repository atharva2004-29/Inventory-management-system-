package org.example.order_inventory_system.controller;

import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.service.CustomerService;
import org.example.order_inventory_system.service.OrderService;
import org.example.order_inventory_system.service.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final ShipmentService shipmentService;

    public CustomerRestController(CustomerService customerService, OrderService orderService, ShipmentService shipmentService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.shipmentService = shipmentService;
    }

    // GET all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    // GET customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    // GET search customers by name
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> search(@RequestParam String name) {
        return ResponseEntity.ok(customerService.searchByName(name));
    }

    // GET all orders for a customer
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Integer id) {
        customerService.findById(id); // throws if not found
        return ResponseEntity.ok(orderService.findByCustomerId(id));
    }

    // GET all shipments for a customer
    @GetMapping("/{id}/shipments")
    public ResponseEntity<List<Shipment>> getShipmentsByCustomer(@PathVariable Integer id) {
        customerService.findById(id); // throws if not found
        return ResponseEntity.ok(shipmentService.findByCustomerId(id));
    }

    // POST create customer
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        customer.setCustomerId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(customer));
    }

    // PUT update customer
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Integer id,
                                           @RequestBody Customer customer) {
        customerService.findById(id); // throws if not found
        customer.setCustomerId(id);
        return ResponseEntity.ok(customerService.save(customer));
    }

    // DELETE customer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        customerService.deleteById(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }

    // GET count
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok((long) customerService.findAll().size());
    }

    // GET export (mock)
    @GetMapping("/export")
    public ResponseEntity<String> exportCsv() {
        return ResponseEntity.ok("Name,Email\nJohn Doe,john@example.com\nJane Smith,jane@example.com");
    }
}
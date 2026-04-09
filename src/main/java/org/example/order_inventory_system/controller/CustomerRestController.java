package org.example.order_inventory_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRestController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getAll() {
        return customerService.findAll();
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Integer id) {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Integer id, @RequestBody Customer customer) {
        customer.setCustomerId(id);
        return customerService.save(customer);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        customerService.deleteById(id);
        return "Deleted successfully";
    }
}
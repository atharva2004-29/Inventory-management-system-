package org.example.order_inventory_system.service;

import org.example.order_inventory_system.exception.CustomerNotFoundException;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> searchByName(String name) {
        return customerRepository.findByFullNameContainingIgnoreCase(name);
    }
}
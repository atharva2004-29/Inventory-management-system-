package org.example.order_inventory_system.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Integer id) {
        super("Customer not found with ID: " + id);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
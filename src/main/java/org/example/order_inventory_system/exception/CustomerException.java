package org.example.order_inventory_system.exception;

public class CustomerException extends RuntimeException {

    public CustomerException(String message) {
        super(message);
    }

    // Static factory methods
    public static CustomerException notFoundById(Integer customerId) {
        return new CustomerException("Customer not found with ID: " + customerId);
    }

    public static CustomerException notFoundByEmail(String email) {
        return new CustomerException("Customer not found with email: " + email);
    }

    public static CustomerException alreadyExists(String email) {
        return new CustomerException("Customer already exists with email: " + email);
    }
}
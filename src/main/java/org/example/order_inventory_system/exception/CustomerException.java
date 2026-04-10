package org.example.order_inventory_system.exception;

public class CustomerException {

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(Integer id) {
            super("Customer not found with ID: " + id);
        }
    }

    public static class AlreadyExistsException extends RuntimeException {
        public AlreadyExistsException(String email) {
            super("Customer already exists with email: " + email);
        }
    }
}
package org.example.order_inventory_system.exception;

public class StoreNotFoundException extends RuntimeException {

    public StoreNotFoundException(Integer id) {
        super("Store not found with ID: " + id);
    }

    public StoreNotFoundException(String message) {
        super(message);
    }

    public static StoreNotFoundException byName(String name) {
        return new StoreNotFoundException("No stores found with name: " + name);
    }
}
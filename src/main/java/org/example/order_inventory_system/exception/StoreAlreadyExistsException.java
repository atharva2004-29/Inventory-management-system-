package org.example.order_inventory_system.exception;


public class StoreAlreadyExistsException extends RuntimeException {

    public StoreAlreadyExistsException(String storeName) {
        super("Store already exists with name: " + storeName);
    }
}

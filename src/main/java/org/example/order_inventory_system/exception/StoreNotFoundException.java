package org.example.order_inventory_system.exception;

public class StoreNotFoundException extends RuntimeException {

    public StoreNotFoundException(Integer storeId) {
        super("Store not found with ID: " + storeId);
    }

    public StoreNotFoundException(String storeName) {
        super("Store not found with name: " + storeName);
    }
}
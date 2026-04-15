package org.example.order_inventory_system.exception;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(Integer id) {
        super("Inventory not found with ID: " + id);
    }

    public InventoryNotFoundException(String message) {
        super(message);
    }

    public static InventoryNotFoundException byProductId(Integer productId) {
        return new InventoryNotFoundException("Inventory not found for Product ID: " + productId);
    }

    public static InventoryNotFoundException byStoreId(Integer storeId) {
        return new InventoryNotFoundException("Inventory not found for Store ID: " + storeId);
    }
}
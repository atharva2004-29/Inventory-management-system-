package org.example.order_inventory_system.exception;

public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException(Integer id) {
        super("Shipment not found with ID: " + id);
    }

    public ShipmentNotFoundException(String message) {
        super(message);
    }

    public static ShipmentNotFoundException byCustomerId(Integer customerId) {
        return new ShipmentNotFoundException("No shipments found for Customer ID: " + customerId);
    }

    public static ShipmentNotFoundException byStoreId(Integer storeId) {
        return new ShipmentNotFoundException("No shipments found for Store ID: " + storeId);
    }

    public static ShipmentNotFoundException byStatus(String status) {
        return new ShipmentNotFoundException("No shipments found with status: " + status);
    }
}
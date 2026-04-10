package org.example.order_inventory_system.exception;

public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException(Integer shipmentId) {
        super("Shipment not found with ID: " + shipmentId);
    }
}
package org.example.order_inventory_system.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Integer id) {
        super("Order not found with ID: " + id);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException byCustomerId(Integer customerId) {
        return new OrderNotFoundException("No orders found for Customer ID: " + customerId);
    }

    public static OrderNotFoundException byStoreId(Integer storeId) {
        return new OrderNotFoundException("No orders found for Store ID: " + storeId);
    }

    public static OrderNotFoundException byStatus(String status) {
        return new OrderNotFoundException("No orders found with status: " + status);
    }
}
package org.example.order_inventory_system.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Integer id) {
        super("Product not found with ID: " + id);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException byName(String name) {
        return new ProductNotFoundException("No products found with name: " + name);
    }
}
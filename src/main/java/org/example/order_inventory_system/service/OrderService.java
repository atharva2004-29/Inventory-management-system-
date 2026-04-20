package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.*;
import org.example.order_inventory_system.repository.InventoryRepository;
import org.example.order_inventory_system.repository.OrderItemRepository;
import org.example.order_inventory_system.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, InventoryRepository inventoryRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderItemRepository = orderItemRepository;
    }



    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void deleteById(Integer id) {
        orderRepository.deleteById(id);
    }

    public List<Order> findByStatus(String status) {
        return orderRepository.findByOrderStatus(status);
    }

    public List<Order> findByCustomerId(Integer customerId) {
        return orderRepository.findByCustomer_CustomerId(customerId);
    }

    public Order placeOrder(Integer customerId, Integer storeId, Integer productId, Integer quantity) {
        // check inventory
        List<Inventory> inventoryList = inventoryRepository.findByStore_StoreId(storeId);
        Inventory inventory = inventoryList.stream()
                .filter(inv -> inv.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Product is not available at the selected store."));

        if (inventory.getProductInventory() < quantity) {
            throw new RuntimeException(
                    "Not enough stock. Available: " + inventory.getProductInventory());
        }

        // deduct inventory by quantity
        inventory.setProductInventory(inventory.getProductInventory() - quantity);
        inventoryRepository.save(inventory);

        // create order
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Store store = new Store();
        store.setStoreId(storeId);

        Order order = new Order();
        order.setCustomer(customer);
        order.setStore(store);
        order.setOrderStatus("OPEN");
        order.setOrderTms(java.time.LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // create order item with quantity
        Product product = new Product();
        product.setProductId(productId);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(savedOrder);
        orderItem.setLineItemId(1);
        orderItem.setProduct(product);
        orderItem.setUnitPrice(inventory.getProduct().getUnitPrice());
        orderItem.setQuantity(quantity); // use actual quantity
        orderItemRepository.save(orderItem);

        return savedOrder;
    }

    public List<Order> findByStoreId(Integer storeId) {
        return orderRepository.findByStore_StoreId(storeId);
    }
}
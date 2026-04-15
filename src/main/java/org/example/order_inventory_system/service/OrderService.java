package org.example.order_inventory_system.service;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.repository.InventoryRepository;
import org.example.order_inventory_system.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository; // ADD THIS


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

    public Order placeOrder(Integer customerId, Integer storeId, Integer productId) {
        // check inventory
        List<Inventory> inventoryList = inventoryRepository.findByStore_StoreId(storeId);
        Inventory inventory = inventoryList.stream()
                .filter(inv -> inv.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Product is not available at the selected store."));

        if (inventory.getProductInventory() <= 0) {
            throw new RuntimeException("Product is out of stock at the selected store.");
        }

        // deduct inventory
        inventory.setProductInventory(inventory.getProductInventory() - 1);
        inventoryRepository.save(inventory);

        // place order
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Store store = new Store();
        store.setStoreId(storeId);

        Order order = new Order();
        order.setCustomer(customer);
        order.setStore(store);
        order.setOrderStatus("OPEN");
        order.setOrderTms(java.time.LocalDateTime.now());

        return orderRepository.save(order);
    }
    public List<Order> findByStoreId(Integer storeId) {
        return orderRepository.findByStore_StoreId(storeId);
    }
    }




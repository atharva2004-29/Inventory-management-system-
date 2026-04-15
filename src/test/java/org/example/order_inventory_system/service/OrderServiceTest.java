package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.*;
import org.example.order_inventory_system.repository.InventoryRepository;
import org.example.order_inventory_system.repository.OrderItemRepository;
import org.example.order_inventory_system.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setup() {
        order = new Order();
        order.setOrderId(1);
    }

    // =========================
    // BASIC METHODS
    // =========================

    @Test
    void testFindAll() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> result = orderService.findAll();

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getOrderId());
    }

    @Test
    void testFindById_NotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.findById(1));

        assertTrue(ex.getMessage().contains("Order not found"));
    }

    @Test
    void testSave() {
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.save(order);

        assertNotNull(result);
        verify(orderRepository).save(order);
    }

    @Test
    void testDeleteById() {
        orderService.deleteById(1);

        verify(orderRepository).deleteById(1);
    }

    @Test
    void testFindByStatus() {
        when(orderRepository.findByOrderStatus("OPEN"))
                .thenReturn(List.of(order));

        List<Order> result = orderService.findByStatus("OPEN");

        assertEquals(1, result.size());
    }

    @Test
    void testFindByCustomerId() {
        when(orderRepository.findByCustomer_CustomerId(10))
                .thenReturn(List.of(order));

        List<Order> result = orderService.findByCustomerId(10);

        assertEquals(1, result.size());
    }

    @Test
    void testFindByStoreId() {
        when(orderRepository.findByStore_StoreId(5))
                .thenReturn(List.of(order));

        List<Order> result = orderService.findByStoreId(5);

        assertEquals(1, result.size());
    }

    // =========================
    // PLACE ORDER (MAIN LOGIC)
    // =========================

    @Test
    void testPlaceOrder_Success() {
        Integer customerId = 1;
        Integer storeId = 1;
        Integer productId = 100;
        Integer quantity = 2;

        Product product = new Product();
        product.setProductId(productId);
        product.setUnitPrice(BigDecimal.valueOf(50.00));

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setProductInventory(10);

        when(inventoryRepository.findByStore_StoreId(storeId))
                .thenReturn(List.of(inventory));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.placeOrder(customerId, storeId, productId, quantity);

        assertNotNull(result);
        assertEquals("OPEN", result.getOrderStatus());

        verify(inventoryRepository).save(any(Inventory.class));
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void testPlaceOrder_ProductNotAvailable() {
        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(new ArrayList<>());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(1, 1, 100, 2));

        assertTrue(ex.getMessage().contains("Product is not available"));
    }

    @Test
    void testPlaceOrder_InsufficientStock() {
        Integer productId = 100;

        Product product = new Product();
        product.setProductId(productId);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setProductInventory(1);

        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(List.of(inventory));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(1, 1, productId, 5));

        assertTrue(ex.getMessage().contains("Not enough stock"));
    }
}
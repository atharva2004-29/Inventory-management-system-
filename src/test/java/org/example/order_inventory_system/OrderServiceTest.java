package org.example.order_inventory_system;
import org.example.order_inventory_system.model.*;
import org.example.order_inventory_system.repository.InventoryRepository;
import org.example.order_inventory_system.repository.OrderItemRepository;
import org.example.order_inventory_system.repository.OrderRepository;
import org.example.order_inventory_system.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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


    // ─── findAll ──────────────────────────────────────────────────────────────

    @Test
    void findAll_ShouldReturnAllOrders() {
        Order o1 = buildOrder(1, "OPEN");
        Order o2 = buildOrder(2, "CLOSED");
        when(orderRepository.findAll()).thenReturn(Arrays.asList(o1, o2));

        List<Order> result = orderService.findAll();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void findAll_WhenNoOrders_ShouldReturnEmptyList() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<Order> result = orderService.findAll();

        assertTrue(result.isEmpty());
    }


    // ─── findById ─────────────────────────────────────────────────────────────

    @Test
    void findById_WhenExists_ShouldReturnOrder() {
        Order order = buildOrder(1, "OPEN");
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getOrderId());
    }

    @Test
    void findById_WhenNotFound_ShouldThrowRuntimeException() {
        when(orderRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.findById(99));

        assertTrue(ex.getMessage().contains("99"));
    }


    // ─── save ─────────────────────────────────────────────────────────────────

    @Test
    void save_ShouldPersistAndReturnOrder() {
        Order order = buildOrder(null, "OPEN");
        Order saved = buildOrder(1, "OPEN");
        when(orderRepository.save(order)).thenReturn(saved);

        Order result = orderService.save(order);

        assertNotNull(result.getOrderId());
        verify(orderRepository).save(order);
    }


    // ─── deleteById ───────────────────────────────────────────────────────────

    @Test
    void deleteById_ShouldInvokeRepositoryDelete() {
        doNothing().when(orderRepository).deleteById(1);

        orderService.deleteById(1);

        verify(orderRepository, times(1)).deleteById(1);
    }


    // ─── findByStatus ─────────────────────────────────────────────────────────

    @Test
    void findByStatus_ShouldReturnMatchingOrders() {
        Order open = buildOrder(1, "OPEN");
        when(orderRepository.findByOrderStatus("OPEN"))
                .thenReturn(Collections.singletonList(open));

        List<Order> result = orderService.findByStatus("OPEN");

        assertEquals(1, result.size());
        assertEquals("OPEN", result.get(0).getOrderStatus());
    }

    @Test
    void findByStatus_WhenNoneMatch_ShouldReturnEmptyList() {
        when(orderRepository.findByOrderStatus("CANCELLED"))
                .thenReturn(Collections.emptyList());

        assertTrue(orderService.findByStatus("CANCELLED").isEmpty());
    }


    // ─── findByCustomerId ─────────────────────────────────────────────────────

    @Test
    void findByCustomerId_ShouldReturnCustomerOrders() {
        Order order = buildOrder(1, "OPEN");
        when(orderRepository.findByCustomer_CustomerId(10))
                .thenReturn(Collections.singletonList(order));

        List<Order> result = orderService.findByCustomerId(10);

        assertEquals(1, result.size());
        verify(orderRepository).findByCustomer_CustomerId(10);
    }


    // ─── findByStoreId ────────────────────────────────────────────────────────

    @Test
    void findByStoreId_ShouldReturnStoreOrders() {
        when(orderRepository.findByStore_StoreId(5))
                .thenReturn(Collections.singletonList(buildOrder(1, "OPEN")));

        List<Order> result = orderService.findByStoreId(5);

        assertEquals(1, result.size());
        verify(orderRepository).findByStore_StoreId(5);
    }


    // ─── placeOrder — happy path ──────────────────────────────────────────────

    @Test
    void placeOrder_WhenStockSufficient_ShouldCreateOrderAndDeductInventory() {
        // arrange
        Inventory inventory = buildInventory(productId(101), 10, 299.99);
        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(Collections.singletonList(inventory));

        Order savedOrder = buildOrder(50, "OPEN");
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());

        // act
        Order result = orderService.placeOrder(1, 1, 101, 3);

        // assert — order returned
        assertNotNull(result);
        assertEquals(50, result.getOrderId());

        // assert — inventory reduced by quantity
        assertEquals(7, inventory.getProductInventory());

        // assert — inventory was saved after deduction
        verify(inventoryRepository).save(inventory);

        // assert — order and order-item both persisted
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void placeOrder_ShouldSetCorrectOrderStatus() {
        Inventory inventory = buildInventory(productId(101), 5, 100.0);
        when(inventoryRepository.findByStore_StoreId(2))
                .thenReturn(Collections.singletonList(inventory));

        Order savedOrder = buildOrder(10, "OPEN");
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());

        orderService.placeOrder(2, 2, 101, 1);

        // capture the order passed to save and verify its status
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());
        assertEquals("OPEN", captor.getValue().getOrderStatus());
    }

    @Test
    void placeOrder_ShouldSetCorrectQuantityOnOrderItem() {
        Inventory inventory = buildInventory(productId(101), 10, 50.0);
        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(Collections.singletonList(inventory));
        when(orderRepository.save(any(Order.class))).thenReturn(buildOrder(1, "OPEN"));

        orderService.placeOrder(1, 1, 101, 4);

        ArgumentCaptor<OrderItem> captor = ArgumentCaptor.forClass(OrderItem.class);
        verify(orderItemRepository).save(captor.capture());
        assertEquals(4, captor.getValue().getQuantity());
    }

    @Test
    void placeOrder_ShouldSetUnitPriceFromInventory() {
        Inventory inventory = buildInventory(productId(101), 10, 199.99);
        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(Collections.singletonList(inventory));
        when(orderRepository.save(any(Order.class))).thenReturn(buildOrder(1, "OPEN"));

        orderService.placeOrder(1, 1, 101, 2);

        ArgumentCaptor<OrderItem> captor = ArgumentCaptor.forClass(OrderItem.class);
        verify(orderItemRepository).save(captor.capture());
        assertEquals(199.99, captor.getValue().getUnitPrice());
    }


    // ─── placeOrder — failure paths ───────────────────────────────────────────

    @Test
    void placeOrder_WhenProductNotAtStore_ShouldThrowException() {
        // inventory exists but for a different product
        Inventory inventory = buildInventory(productId(999), 10, 50.0);
        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(Collections.singletonList(inventory));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(1, 1, 101, 1));

        assertTrue(ex.getMessage().contains("not available"));
        // no order should be created
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_WhenInsufficientStock_ShouldThrowException() {
        Inventory inventory = buildInventory(productId(101), 2, 50.0);
        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(Collections.singletonList(inventory));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(1, 1, 101, 5));

        assertTrue(ex.getMessage().contains("Not enough stock"));
        assertTrue(ex.getMessage().contains("2")); // shows available qty

        // inventory must NOT be mutated
        assertEquals(2, inventory.getProductInventory());
        verify(inventoryRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_WhenStoreHasNoInventoryAtAll_ShouldThrowException() {
        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(1, 1, 101, 1));
    }

    @Test
    void placeOrder_WhenQuantityEqualsStock_ShouldSucceedAndReduceToZero() {
        Inventory inventory = buildInventory(productId(101), 3, 75.0);
        when(inventoryRepository.findByStore_StoreId(1))
                .thenReturn(Collections.singletonList(inventory));
        when(orderRepository.save(any(Order.class))).thenReturn(buildOrder(1, "OPEN"));

        orderService.placeOrder(1, 1, 101, 3);

        assertEquals(0, inventory.getProductInventory());
        verify(inventoryRepository).save(inventory);
    }


    // ─── helpers ──────────────────────────────────────────────────────────────

    private Order buildOrder(Integer id, String status) {
        Order o = new Order();
        o.setOrderId(id);
        o.setOrderStatus(status);
        o.setOrderTms(LocalDateTime.now());
        Customer c = new Customer();
        c.setCustomerId(1);
        o.setCustomer(c);
        Store s = new Store();
        s.setStoreId(1);
        o.setStore(s);
        return o;
    }

    private Product productId(Integer id) {
        Product p = new Product();
        p.setProductId(id);
        p.setUnitPrice(BigDecimal.valueOf(50.0));
        return p;
    }

    private Inventory buildInventory(Product product, int qty, double price) {
        product.setUnitPrice(BigDecimal.valueOf(price));
        Inventory inv = new Inventory();
        inv.setProduct(product);
        inv.setProductInventory(qty);
        Store store = new Store();
        store.setStoreId(1);
        inv.setStore(store);
        return inv;
    }
}
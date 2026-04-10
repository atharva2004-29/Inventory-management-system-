package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.*;
import org.example.order_inventory_system.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    private Shipment shipment;
    private Customer customer;
    private Store store;
    private Order order;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setFullName("John Doe");
        customer.setEmailAddress("john@example.com");

        store = new Store();
        store.setStoreId(1);
        store.setStoreName("Mumbai Central");

        order = new Order();
        order.setOrderId(1);
        order.setCustomer(customer);
        order.setStore(store);
        order.setOrderStatus("PAID");
        order.setOrderTms(LocalDateTime.now());

        shipment = new Shipment();
        shipment.setShipmentId(1);
//        shipment.setOrder(order);
        shipment.setCustomer(customer);
        shipment.setStore(store);
        shipment.setDeliveryAddress("123 MG Road, Mumbai");
        shipment.setShipmentStatus("CREATED");
    }

    @Test
    void findAll_ShouldReturnAllShipments() {
        when(shipmentRepository.findAll()).thenReturn(List.of(shipment));

        List<Shipment> result = shipmentService.findAll();

        assertEquals(1, result.size());
        assertEquals("CREATED", result.get(0).getShipmentStatus());
        verify(shipmentRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnShipment_WhenExists() {
        when(shipmentRepository.findById(1)).thenReturn(Optional.of(shipment));

        Shipment result = shipmentService.findById(1);

        assertNotNull(result);
        assertEquals("CREATED", result.getShipmentStatus());
        verify(shipmentRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(shipmentRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> shipmentService.findById(99));

        assertEquals("Shipment not found: 99", ex.getMessage());
    }

    @Test
    void save_ShouldReturnSavedShipment() {
        when(shipmentRepository.save(shipment)).thenReturn(shipment);

        Shipment result = shipmentService.save(shipment);

        assertNotNull(result);
        assertEquals("CREATED", result.getShipmentStatus());
        verify(shipmentRepository, times(1)).save(shipment);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(shipmentRepository).deleteById(1);

        shipmentService.deleteById(1);

        verify(shipmentRepository, times(1)).deleteById(1);
    }

    @Test
    void findByStatus_ShouldReturnMatchingShipments() {
        when(shipmentRepository.findByShipmentStatus("CREATED")).thenReturn(List.of(shipment));

        List<Shipment> result = shipmentService.findByStatus("CREATED");

        assertEquals(1, result.size());
        assertEquals("CREATED", result.get(0).getShipmentStatus());
    }

    @Test
    void findByCustomerId_ShouldReturnShipmentsForCustomer() {
        when(shipmentRepository.findByCustomer_CustomerId(1)).thenReturn(List.of(shipment));

        List<Shipment> result = shipmentService.findByCustomerId(1);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getCustomer().getFullName());
    }

    @Test
    void findByStoreId_ShouldReturnShipmentsForStore() {
        when(shipmentRepository.findByStore_StoreId(1)).thenReturn(List.of(shipment));

        List<Shipment> result = shipmentService.findByStoreId(1);

        assertEquals(1, result.size());
        assertEquals("Mumbai Central", result.get(0).getStore().getStoreName());
    }

    @Test
    void findByOrderId_ShouldReturnShipmentsForOrder() {
        when(shipmentRepository.findByOrder_OrderId(1)).thenReturn(List.of(shipment));

        List<Shipment> result = shipmentService.findByOrderId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getOrder().getOrderId());
    }
}
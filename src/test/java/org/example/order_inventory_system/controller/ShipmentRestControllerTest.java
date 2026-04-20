package org.example.order_inventory_system.controller;

import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentRestControllerTest {

    @Mock
    private ShipmentService shipmentService;

    @InjectMocks
    private ShipmentRestController shipmentRestController;

    // ✅ Helper
    private Shipment createShipment() {
        Shipment shipment = new Shipment();
        shipment.setShipmentId(1);

        Store store = new Store();
        store.setStoreId(1);

        Customer customer = new Customer();
        customer.setCustomerId(1);

        shipment.setStore(store);
        shipment.setCustomer(customer);
        shipment.setDeliveryAddress("Pune");
        shipment.setShipmentStatus("DELIVERED");

        return shipment;
    }

    // ✅ GET ALL
    @Test
    void testGetAll() {
        Shipment shipment = createShipment();

        when(shipmentService.findAll()).thenReturn(List.of(shipment));

        ResponseEntity<List<Shipment>> response =
                shipmentRestController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    // ✅ GET BY ID
    @Test
    void testGetById() {
        Shipment shipment = createShipment();

        when(shipmentService.findById(1)).thenReturn(shipment);

        ResponseEntity<Shipment> response =
                shipmentRestController.getById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getShipmentId());
    }

    // ✅ GET BY STATUS
    @Test
    void testGetByStatus() {
        Shipment shipment = createShipment();

        when(shipmentService.findByStatus("DELIVERED"))
                .thenReturn(List.of(shipment));

        ResponseEntity<List<Shipment>> response =
                shipmentRestController.getByStatus("DELIVERED");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("DELIVERED",
                response.getBody().get(0).getShipmentStatus());
    }

    @Test
    void testCreate() {
        Shipment shipment = createShipment();

        when(shipmentService.save(any(Shipment.class)))
                .thenAnswer(invocation -> {
                    Shipment s = invocation.getArgument(0);
                    s.setShipmentId(1); // simulate DB-generated ID
                    return s;
                });

        ResponseEntity<Shipment> response =
                shipmentRestController.create(shipment);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getShipmentId());
    }

    // ✅ UPDATE
    @Test
    void testUpdate() {
        Shipment shipment = createShipment();

        when(shipmentService.findById(1)).thenReturn(shipment);
        when(shipmentService.save(any(Shipment.class)))
                .thenReturn(shipment);

        ResponseEntity<Shipment> response =
                shipmentRestController.update(1, shipment);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getShipmentId());
    }

    // ✅ DELETE
    @Test
    void testDelete() {
        doNothing().when(shipmentService).deleteById(1);

        ResponseEntity<Void> response =
                shipmentRestController.delete(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(shipmentService, times(1)).deleteById(1);
    }
}
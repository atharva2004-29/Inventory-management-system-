package org.example.order_inventory_system;

import org.example.order_inventory_system.exception.ShipmentNotFoundException;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.repository.ShipmentRepository;

import org.example.order_inventory_system.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentService ShipmentService;

    // ✅ findAll
    @Test
    void testFindAll() {
        when(shipmentRepository.findAll()).thenReturn(List.of(new Shipment(), new Shipment()));

        List<Shipment> result = ShipmentService.findAll();

        assertEquals(2, result.size());
    }

    // ✅ findById success
    @Test
    void testFindById_Success() {
        Shipment shipment = new Shipment();
        shipment.setShipmentId(1);

        when(shipmentRepository.findById(1)).thenReturn(Optional.of(shipment));

        Shipment result = ShipmentService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getShipmentId());
    }

    // ❌ findById not found
    @Test
    void testFindById_NotFound() {
        when(shipmentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ShipmentNotFoundException.class, () -> {
            ShipmentService.findById(1);
        });
    }

    // ✅ save
    @Test
    void testSave() {
        Shipment shipment = new Shipment();
        shipment.setShipmentStatus("PENDING");

        when(shipmentRepository.save(shipment)).thenReturn(shipment);

        Shipment result = ShipmentService.save(shipment);

        assertEquals("PENDING", result.getShipmentStatus());
        verify(shipmentRepository, times(1)).save(shipment);
    }

    // ✅ delete
    @Test
    void testDeleteById() {
        doNothing().when(shipmentRepository).deleteById(1);

        ShipmentService.deleteById(1);

        verify(shipmentRepository, times(1)).deleteById(1);
    }

    // ✅ findByStatus
    @Test
    void testFindByStatus() {
        when(shipmentRepository.findByShipmentStatus("DELIVERED"))
                .thenReturn(List.of(new Shipment(), new Shipment()));

        List<Shipment> result = ShipmentService.findByStatus("DELIVERED");

        assertEquals(2, result.size());
    }

    // ✅ findByCustomerId
    @Test
    void testFindByCustomerId() {
        when(shipmentRepository.findByCustomer_CustomerId(1))
                .thenReturn(List.of(new Shipment()));

        List<Shipment> result = ShipmentService.findByCustomerId(1);

        assertEquals(1, result.size());
    }

    // ✅ findByStoreId
    @Test
    void testFindByStoreId() {
        when(shipmentRepository.findByStore_StoreId(1))
                .thenReturn(List.of(new Shipment(), new Shipment(), new Shipment()));

        List<Shipment> result = ShipmentService.findByStoreId(1);

        assertEquals(3, result.size());
    }
}
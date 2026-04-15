package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory inventory;

    @BeforeEach
    void setup() {
        inventory = new Inventory();
        inventory.setInventoryId(1);
        inventory.setProductInventory(10);
    }

    // =========================
    // BASIC METHODS
    // =========================

    @Test
    void testFindAll() {
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));

        List<Inventory> result = inventoryService.findAll();

        assertEquals(1, result.size());
        verify(inventoryRepository).findAll();
    }

    @Test
    void testFindById_Success() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));

        Inventory result = inventoryService.findById(1);

        assertNotNull(result);
        assertEquals(10, result.getProductInventory());
    }

    @Test
    void testFindById_NotFound() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventoryService.findById(1));

        assertTrue(ex.getMessage().contains("Inventory not found"));
    }

    @Test
    void testSave() {
        when(inventoryRepository.save(inventory)).thenReturn(inventory);

        Inventory result = inventoryService.save(inventory);

        assertNotNull(result);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testDeleteById() {
        inventoryService.deleteById(1);

        verify(inventoryRepository).deleteById(1);
    }

    // =========================
    // FILTER METHODS
    // =========================

    @Test
    void testFindByStoreId() {
        when(inventoryRepository.findByStore_StoreId(5))
                .thenReturn(List.of(inventory));

        List<Inventory> result = inventoryService.findByStoreId(5);

        assertEquals(1, result.size());
    }

    @Test
    void testFindLowStock() {
        when(inventoryRepository.findByProductInventoryLessThan(5))
                .thenReturn(List.of(inventory));

        List<Inventory> result = inventoryService.findLowStock(5);

        assertEquals(1, result.size());
    }

//   // @Test
//   // void testFindAllByProductId() {
//     //   when(inventoryRepository.findAllByProduct_ProductId(100))
//                .thenReturn(List.of(inventory));
//
//        List<Inventory> result = inventoryService.findAllByProductId(100);
//
//        assertEquals(1, result.size());
//    }

    // =========================
    // UPDATE LOGIC
    // =========================

    @Test
    void testUpdateQuantity() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Inventory result = inventoryService.updateQuantity(1, 20);

        assertEquals(20, result.getProductInventory());
        verify(inventoryRepository).save(inventory);
    }

    // =========================
    // RESERVE STOCK
    // =========================

    @Test
    void testReserveStock_Success() {
        when(inventoryRepository.findByProduct_ProductId(100))
                .thenReturn(Optional.of(inventory));

        boolean result = inventoryService.reserveStock(100, 5);

        assertTrue(result);
        assertEquals(5, inventory.getProductInventory());
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testReserveStock_InsufficientStock() {
        when(inventoryRepository.findByProduct_ProductId(100))
                .thenReturn(Optional.of(inventory)); // inventory = 10

        boolean result = inventoryService.reserveStock(100, 20);

        assertFalse(result);
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void testReserveStock_ProductNotFound() {
        when(inventoryRepository.findByProduct_ProductId(100))
                .thenReturn(Optional.empty());

        boolean result = inventoryService.reserveStock(100, 5);

        assertFalse(result);
    }

    // =========================
    // RELEASE STOCK
    // =========================

    @Test
    void testReleaseStock_Success() {
        when(inventoryRepository.findByProduct_ProductId(100))
                .thenReturn(Optional.of(inventory));

        inventoryService.releaseStock(100, 5);

        assertEquals(15, inventory.getProductInventory());
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void testReleaseStock_ProductNotFound() {
        when(inventoryRepository.findByProduct_ProductId(100))
                .thenReturn(Optional.empty());

        inventoryService.releaseStock(100, 5);

        verify(inventoryRepository, never()).save(any());
    }
}
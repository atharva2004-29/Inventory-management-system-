package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.repository.StoreRepository;
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
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private Store store;

    @BeforeEach
    void setup() {
        store = new Store();
        store.setStoreId(1);
        store.setStoreName("Test Store");
    }

    // =========================
    // BASIC METHODS
    // =========================

    @Test
    void testFindAll() {
        when(storeRepository.findAll()).thenReturn(List.of(store));

        List<Store> result = storeService.findAll();

        assertEquals(1, result.size());
        verify(storeRepository).findAll();
    }

    @Test
    void testFindById_Success() {
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));

        Store result = storeService.findById(1);

        assertNotNull(result);
        assertEquals("Test Store", result.getStoreName());
    }

    @Test
    void testFindById_NotFound() {
        when(storeRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> storeService.findById(1));

        assertTrue(ex.getMessage().contains("Store not found"));
    }

    @Test
    void testSave() {
        when(storeRepository.save(store)).thenReturn(store);

        Store result = storeService.save(store);

        assertNotNull(result);
        verify(storeRepository).save(store);
    }

//    @Test
//    void testDeleteById() {
//        storeService.deleteById(1);
//
//        verify(storeRepository).deleteById(1);
//    }

    // =========================
    // SEARCH FUNCTION
    // =========================

    @Test
    void testSearchByName() {
        when(storeRepository.findByStoreNameContainingIgnoreCase("test"))
                .thenReturn(List.of(store));

        List<Store> result = storeService.searchByName("test");

        assertEquals(1, result.size());
        assertEquals("Test Store", result.get(0).getStoreName());
    }
}
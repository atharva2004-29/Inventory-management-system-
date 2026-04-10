package org.example.order_inventory_system.service;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
    void setUp() {
        store = new Store();
        store.setStoreId(1);
        store.setStoreName("Mumbai Central");
        store.setPhysicalAddress("123 MG Road, Mumbai");
        store.setWebAddress("www.mumbaicentral.com");
    }

    @Test
    void findAll_ShouldReturnAllStores() {
        when(storeRepository.findAll()).thenReturn(List.of(store));

        List<Store> result = storeService.findAll();

        assertEquals(1, result.size());
        assertEquals("Mumbai Central", result.get(0).getStoreName());
        verify(storeRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnStore_WhenExists() {
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));

        Store result = storeService.findById(1);

        assertNotNull(result);
        assertEquals("Mumbai Central", result.getStoreName());
        verify(storeRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(storeRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> storeService.findById(99));

        assertEquals("Store not found: 99", ex.getMessage());
    }

    @Test
    void save_ShouldReturnSavedStore() {
        when(storeRepository.save(store)).thenReturn(store);

        Store result = storeService.save(store);

        assertNotNull(result);
        assertEquals("Mumbai Central", result.getStoreName());
        verify(storeRepository, times(1)).save(store);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(storeRepository).deleteById(1);

        storeService.deleteById(1);

        verify(storeRepository, times(1)).deleteById(1);
    }

    @Test
    void searchByName_ShouldReturnMatchingStores() {
        when(storeRepository.findByStoreNameContainingIgnoreCase("mumbai"))
                .thenReturn(List.of(store));

        List<Store> result = storeService.searchByName("mumbai");

        assertEquals(1, result.size());
        assertEquals("Mumbai Central", result.get(0).getStoreName());
    }
}
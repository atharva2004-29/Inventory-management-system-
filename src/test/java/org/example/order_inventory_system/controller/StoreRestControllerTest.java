package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.service.InventoryService;
import org.example.order_inventory_system.service.OrderService;
import org.example.order_inventory_system.service.ShipmentService;
import org.example.order_inventory_system.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StoreRestControllerTest {

    private MockMvc mockMvc;

    @Mock private StoreService storeService;
    @Mock private InventoryService inventoryService;
    @Mock private OrderService orderService;
    @Mock private ShipmentService shipmentService;

    @InjectMocks
    private StoreRestController storeRestController;

    private ObjectMapper objectMapper;
    private Store sampleStore;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(storeRestController).build();
        objectMapper = new ObjectMapper();

        sampleStore = new Store();
        sampleStore.setStoreId(1);
        sampleStore.setStoreName("Main Street Branch");
        sampleStore.setWebAddress("https://mainstreet.com");
        // Ensure other @NotNull fields in your Store model are set here
    }

    @Test
    void getAll_ShouldReturnStores() throws Exception {
        when(storeService.findAll()).thenReturn(Collections.singletonList(sampleStore));

        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeName").value("Main Street Branch"));
    }

    @Test
    void getInventory_ShouldReturnInventoryList() throws Exception {
        Inventory item = new Inventory();
        // Populate inventory fields as needed

        when(storeService.findById(1)).thenReturn(sampleStore);
        when(inventoryService.findByStoreId(1)).thenReturn(Collections.singletonList(item));

        mockMvc.perform(get("/api/stores/1/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void create_ShouldReturnCreatedStatus() throws Exception {
        // We use sampleStore which has required fields to pass @Valid
        when(storeService.save(any(Store.class))).thenReturn(sampleStore);

        mockMvc.perform(post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStore)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.storeId").value(1));
    }

    @Test
    void update_ShouldReturnOkStatus() throws Exception {
        when(storeService.findById(1)).thenReturn(sampleStore);
        when(storeService.save(any(Store.class))).thenReturn(sampleStore);

        mockMvc.perform(put("/api/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStore)))
                .andExpect(status().isOk());

        verify(storeService).findById(1);
        verify(storeService).save(any(Store.class));
    }

//    @Test
//    void delete_ShouldReturnNoContent() throws Exception {
//        doNothing().when(storeService).deleteById(1);
//
//        mockMvc.perform(delete("/api/stores/1"))
//                .andExpect(status().isNoContent());
//
//        verify(storeService, times(1)).deleteById(1);
//    }
}
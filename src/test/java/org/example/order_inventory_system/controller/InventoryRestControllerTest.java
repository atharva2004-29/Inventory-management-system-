package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.service.InventoryService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InventoryRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryRestController inventoryRestController;

    private ObjectMapper objectMapper;
    private Inventory sampleInventory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryRestController).build();
        objectMapper = new ObjectMapper();

        Product product = new Product();
        product.setProductId(1);

        Store store = new Store();
        store.setStoreId(1);

        sampleInventory = new Inventory();
        sampleInventory.setInventoryId(1);
        sampleInventory.setProduct(product);
        sampleInventory.setStore(store);
        sampleInventory.setProductInventory(50);
    }

    @Test
    void getAll_ShouldReturnInventoryList() throws Exception {
        when(inventoryService.findAll()).thenReturn(Collections.singletonList(sampleInventory));

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].inventoryId").value(1))
                .andExpect(jsonPath("$[0].productInventory").value(50));
    }

    @Test
    void getById_ShouldReturnInventory() throws Exception {
        when(inventoryService.findById(1)).thenReturn(sampleInventory);

        mockMvc.perform(get("/api/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inventoryId").value(1));
    }

//    @Test
//    void getByProductId_ShouldReturnInventory() throws Exception {
//        when(inventoryService.findByProductId(1)).thenReturn(Optional.of(sampleInventory));
//
//        mockMvc.perform(get("/api/inventory/product/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.productInventory").value(50));
//    }

    @Test
    void getByStoreId_ShouldReturnInventoryList() throws Exception {
        when(inventoryService.findByStoreId(1)).thenReturn(Collections.singletonList(sampleInventory));

        mockMvc.perform(get("/api/inventory/store/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getLowStock_ShouldReturnInventoryList() throws Exception {
        when(inventoryService.findLowStock(10)).thenReturn(Collections.singletonList(sampleInventory));

        mockMvc.perform(get("/api/inventory/low-stock").param("threshold", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productInventory").value(50));
    }

    @Test
    void create_ShouldReturnCreatedStatus() throws Exception {
        when(inventoryService.save(any(Inventory.class))).thenReturn(sampleInventory);

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleInventory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inventoryId").value(1));
    }

    @Test
    void update_ShouldReturnOkStatus() throws Exception {
        when(inventoryService.findById(1)).thenReturn(sampleInventory);
        when(inventoryService.save(any(Inventory.class))).thenReturn(sampleInventory);

        mockMvc.perform(put("/api/inventory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleInventory)))
                .andExpect(status().isOk());

        verify(inventoryService).findById(1);
        verify(inventoryService).save(any(Inventory.class));
    }

    @Test
    void updateQuantity_ShouldReturnUpdatedInventory() throws Exception {
        sampleInventory.setProductInventory(25);
        when(inventoryService.updateQuantity(1, 25)).thenReturn(sampleInventory);

        mockMvc.perform(patch("/api/inventory/1/quantity").param("productInventory", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productInventory").value(25));
    }

    @Test
    void reserveStock_ShouldReturnSuccess_WhenStockAvailable() throws Exception {
        when(inventoryService.reserveStock(1, 5)).thenReturn(true);

        mockMvc.perform(post("/api/inventory/reserve")
                        .param("productId", "1")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock reserved successfully"));
    }

    @Test
    void reserveStock_ShouldReturnBadRequest_WhenInsufficientStock() throws Exception {
        when(inventoryService.reserveStock(1, 999)).thenReturn(false);

        mockMvc.perform(post("/api/inventory/reserve")
                        .param("productId", "1")
                        .param("quantity", "999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient stock"));
    }

    @Test
    void releaseStock_ShouldReturnSuccess() throws Exception {
        doNothing().when(inventoryService).releaseStock(1, 5);

        mockMvc.perform(post("/api/inventory/release")
                        .param("productId", "1")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock released successfully"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(inventoryService).deleteById(1);

        mockMvc.perform(delete("/api/inventory/1"))
                .andExpect(status().isNoContent());

        verify(inventoryService, times(1)).deleteById(1);
    }
}

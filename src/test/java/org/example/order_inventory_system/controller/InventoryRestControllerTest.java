package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InventoryRestController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*Converter"
    )
)
public class InventoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InventoryService inventoryService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Inventory sampleInventory;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId(50);
        
        Store store = new Store();
        store.setStoreId(10);

        sampleInventory = new Inventory();
        sampleInventory.setInventoryId(1);
        sampleInventory.setProduct(product);
        sampleInventory.setStore(store);
        sampleInventory.setProductInventory(100);
    }

    @Test
    @WithMockUser
    void getAll_ShouldReturnList() throws Exception {
        when(inventoryService.findAll()).thenReturn(List.of(sampleInventory));

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productInventory").value(100));
    }

    @Test
    @WithMockUser
    void getLowStock_ShouldReturnFilteredList() throws Exception {
        when(inventoryService.findLowStock(10)).thenReturn(List.of(sampleInventory));

        mockMvc.perform(get("/api/inventory/low-stock").param("threshold", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreatedStatus() throws Exception {
        when(inventoryService.save(any(Inventory.class))).thenReturn(sampleInventory);

        mockMvc.perform(post("/api/inventory")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleInventory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inventoryId").value(1));
    }

    @Test
    @WithMockUser
    void updateQuantity_ShouldUpdateSpecificField() throws Exception {
        sampleInventory.setProductInventory(150);
        when(inventoryService.updateQuantity(anyInt(), anyInt())).thenReturn(sampleInventory);

        mockMvc.perform(patch("/api/inventory/1/quantity")
                        .with(csrf())
                        .param("productInventory", "150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productInventory").value(150));
    }

    @Test
    @WithMockUser
    void reserveStock_ShouldReturnOk_WhenSuccessful() throws Exception {
        when(inventoryService.reserveStock(50, 5)).thenReturn(true);

        mockMvc.perform(post("/api/inventory/reserve")
                        .with(csrf())
                        .param("productId", "50")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock reserved successfully"));
    }

    @Test
    @WithMockUser
    void reserveStock_ShouldReturnBadRequest_WhenInsufficient() throws Exception {
        when(inventoryService.reserveStock(50, 5000)).thenReturn(false);

        mockMvc.perform(post("/api/inventory/reserve")
                        .with(csrf())
                        .param("productId", "50")
                        .param("quantity", "5000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient stock"));
    }

    @Test
    @WithMockUser
    void releaseStock_ShouldReturnOk() throws Exception {
        doNothing().when(inventoryService).releaseStock(50, 5);

        mockMvc.perform(post("/api/inventory/release")
                        .with(csrf())
                        .param("productId", "50")
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock released successfully"));
    }

    @Test
    @WithMockUser
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(inventoryService).deleteById(1);

        mockMvc.perform(delete("/api/inventory/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
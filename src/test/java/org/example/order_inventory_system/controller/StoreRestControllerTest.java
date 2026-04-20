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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StoreRestController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*Converter"
    )
)
public class StoreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- Service Mocks ---
    @MockitoBean private StoreService storeService;
    @MockitoBean private InventoryService inventoryService;
    @MockitoBean private OrderService orderService;
    @MockitoBean private ShipmentService shipmentService;

    // --- Security Mock ---
    @MockitoBean private UserDetailsService userDetailsService;

    private Store sampleStore;

    @BeforeEach
    void setUp() {
        sampleStore = new Store();
        sampleStore.setStoreId(1);
        sampleStore.setStoreName("Main Street Hub");
        sampleStore.setWebAddress("https://mainstreet.example.com");
        sampleStore.setPhysicalAddress("123 Main St, Springfield");
    }

    @Test
    @WithMockUser
    void getAll_ShouldReturnList() throws Exception {
        when(storeService.findAll()).thenReturn(List.of(sampleStore));

        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeName").value("Main Street Hub"));
    }

    @Test
    @WithMockUser
    void getById_ShouldReturnStore() throws Exception {
        when(storeService.findById(1)).thenReturn(sampleStore);

        mockMvc.perform(get("/api/stores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName").value("Main Street Hub"));
    }

    @Test
    @WithMockUser
    void search_ShouldReturnMatches() throws Exception {
        when(storeService.searchByName("Main")).thenReturn(List.of(sampleStore));

        mockMvc.perform(get("/api/stores/search").param("name", "Main"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getInventory_ShouldReturnList() throws Exception {
        Inventory inv = new Inventory(); // Assuming Inventory model is available
        inv.setInventoryId(101);

        when(storeService.findById(1)).thenReturn(sampleStore);
        when(inventoryService.findByStoreId(1)).thenReturn(List.of(inv));

        mockMvc.perform(get("/api/stores/1/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].inventoryId").value(101));
    }

    @Test
    @WithMockUser
    void getOrders_ShouldReturnList() throws Exception {
        Order order = new Order();
        order.setOrderId(201);

        when(storeService.findById(1)).thenReturn(sampleStore);
        when(orderService.findByStoreId(1)).thenReturn(List.of(order));

        mockMvc.perform(get("/api/stores/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(201));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreatedStatus() throws Exception {
        when(storeService.save(any(Store.class))).thenReturn(sampleStore);

        mockMvc.perform(post("/api/stores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStore)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.storeId").value(1));
    }

    @Test
    @WithMockUser
    void update_ShouldReturnUpdatedStore() throws Exception {
        when(storeService.findById(1)).thenReturn(sampleStore);
        when(storeService.save(any(Store.class))).thenReturn(sampleStore);

        mockMvc.perform(put("/api/stores/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStore)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName").value("Main Street Hub"));
    }

    @Test
    @WithMockUser
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(storeService).deleteById(1);

        mockMvc.perform(delete("/api/stores/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
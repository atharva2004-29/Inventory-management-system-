package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.service.OrderService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderRestController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*Converter" // Prevents context crashes from StoreConverter, etc.
    )
)
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        // Setup Customer and Store to satisfy @NotNull constraints in Order model
        Customer customer = new Customer();
        customer.setCustomerId(1);

        Store store = new Store();
        store.setStoreId(10);

        sampleOrder = new Order();
        sampleOrder.setOrderId(100);
        sampleOrder.setCustomer(customer);
        sampleOrder.setStore(store);
        sampleOrder.setOrderStatus("OPEN");
        sampleOrder.setOrderTms(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    void getAll_ShouldReturnList() throws Exception {
        when(orderService.findAll()).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].orderStatus").value("OPEN"));
    }

    @Test
    @WithMockUser
    void getById_ShouldReturnOrder() throws Exception {
        when(orderService.findById(100)).thenReturn(sampleOrder);

        mockMvc.perform(get("/api/orders/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(100));
    }

    @Test
    @WithMockUser
    void getByStatus_ShouldReturnList() throws Exception {
        when(orderService.findByStatus("OPEN")).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders/status/OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderStatus").value("OPEN"));
    }

    @Test
    @WithMockUser
    void getByCustomer_ShouldReturnList() throws Exception {
        when(orderService.findByCustomerId(1)).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer.customerId").value(1));
    }

    @Test
    @WithMockUser
    void getByStore_ShouldReturnList() throws Exception {
        when(orderService.findByStoreId(10)).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders/store/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].store.storeId").value(10));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreated() throws Exception {
        when(orderService.save(any(Order.class))).thenReturn(sampleOrder);

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderStatus").value("OPEN"));
    }

    @Test
    @WithMockUser
    void update_ShouldReturnUpdated() throws Exception {
        when(orderService.findById(100)).thenReturn(sampleOrder);
        when(orderService.save(any(Order.class))).thenReturn(sampleOrder);

        mockMvc.perform(put("/api/orders/100")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(100));
    }

    @Test
    @WithMockUser
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(orderService).deleteById(100);

        mockMvc.perform(delete("/api/orders/100")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
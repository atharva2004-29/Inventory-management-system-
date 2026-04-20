package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.service.CustomerService;
import org.example.order_inventory_system.service.OrderService;
import org.example.order_inventory_system.service.ShipmentService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerRestController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = ".*Converter" // Keeps Repositories out of the Web Slice
        )
)
public class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- Mocks for Services ---
    @MockitoBean private CustomerService customerService;
    @MockitoBean private OrderService orderService;
    @MockitoBean private ShipmentService shipmentService;

    // --- Mock for Security ---
    @MockitoBean private UserDetailsService userDetailsService;

    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer();
        sampleCustomer.setCustomerId(1);
        sampleCustomer.setFullName("John Doe");
        sampleCustomer.setEmailAddress("john@example.com");
    }

    @Test
    @WithMockUser
    void getAll_ShouldReturnList() throws Exception {
        when(customerService.findAll()).thenReturn(List.of(sampleCustomer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    @WithMockUser
    void getById_ShouldReturnCustomer() throws Exception {
        when(customerService.findById(1)).thenReturn(sampleCustomer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress").value("john@example.com"));
    }

    @Test
    @WithMockUser
    void search_ShouldReturnMatches() throws Exception {
        when(customerService.searchByName("John")).thenReturn(List.of(sampleCustomer));

        mockMvc.perform(get("/api/customers/search").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreated() throws Exception {
        when(customerService.save(any(Customer.class))).thenReturn(sampleCustomer);

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    @WithMockUser
    void update_ShouldReturnUpdated() throws Exception {
        when(customerService.findById(1)).thenReturn(sampleCustomer);
        when(customerService.save(any(Customer.class))).thenReturn(sampleCustomer);

        mockMvc.perform(put("/api/customers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    @WithMockUser
    void delete_ShouldReturnSuccess() throws Exception {
        doNothing().when(customerService).deleteById(1);

        mockMvc.perform(delete("/api/customers/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer deleted successfully"));
    }
}
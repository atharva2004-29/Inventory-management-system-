package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.model.OrderItem;
import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.service.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderItemRestController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*Converter" // Prevents context crashes from Repositories used in Converters
    )
)
public class OrderItemRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderItemService orderItemService;

    @MockitoBean
    private UserDetailsService userDetailsService; // Mocking security layer

    private OrderItem sampleItem;

    @BeforeEach
    void setUp() {
        Order order = new Order();
        order.setOrderId(100);

        Product product = new Product();
        product.setProductId(50);
        product.setProductName("Test Widget");

        sampleItem = new OrderItem();
        sampleItem.setOrderId(100); 
        sampleItem.setLineItemId(1);
        sampleItem.setOrder(order);
        sampleItem.setProduct(product);
        sampleItem.setUnitPrice(new BigDecimal("19.99"));
        sampleItem.setQuantity(2);
    }

    // --- HAPPY PATH TESTS ---

    @Test
    @WithMockUser
    @DisplayName("GET /order/{id} - Should return list of items")
    void getByOrderId_ShouldReturnList() throws Exception {
        when(orderItemService.findByOrderId(100)).thenReturn(List.of(sampleItem));

        mockMvc.perform(get("/api/order-items/order/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].unitPrice").value(19.99));
    }

    @Test
    @WithMockUser
    @DisplayName("POST / - Should create item and return 201")
    void create_ShouldReturnCreatedStatus() throws Exception {
        when(orderItemService.save(any(OrderItem.class))).thenReturn(sampleItem);

        mockMvc.perform(post("/api/order-items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(100));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /{id} - Should return 204")
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(orderItemService).deleteById(100);

        mockMvc.perform(delete("/api/order-items/100")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // --- EDGE CASE & NEGATIVE TESTS ---

    @Test
    @WithMockUser
    @DisplayName("POST / - Should return 400 when quantity is 0")
    void create_ShouldReturnBadRequest_WhenQuantityIsInvalid() throws Exception {
        sampleItem.setQuantity(0); // Violates @Min(1)

        mockMvc.perform(post("/api/order-items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleItem)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("POST / - Should return 400 when product is missing")
    void create_ShouldReturnBadRequest_WhenProductIsNull() throws Exception {
        sampleItem.setProduct(null); // Violates @NotNull

        mockMvc.perform(post("/api/order-items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleItem)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /order/{id} - Should return 401 when not logged in")
    void getByOrderId_ShouldReturnUnauthorized_WhenNoUser() throws Exception {
        // No @WithMockUser annotation here
        mockMvc.perform(get("/api/order-items/order/100"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /order/{id} - Should return empty list when no items exist")
    void getByOrderId_ShouldReturnEmpty_WhenNotFound() throws Exception {
        when(orderItemService.findByOrderId(999)).thenReturn(List.of());

        mockMvc.perform(get("/api/order-items/order/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
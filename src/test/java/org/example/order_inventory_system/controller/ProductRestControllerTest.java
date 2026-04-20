package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.service.InventoryService;
import org.example.order_inventory_system.service.ProductService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductRestController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*Converter"
    )
)
public class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private InventoryService inventoryService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setProductId(1);
        sampleProduct.setProductName("Running Shoes");
        sampleProduct.setUnitPrice(new BigDecimal("89.99"));
        sampleProduct.setColour("Blue");
        sampleProduct.setBrand("Nike");
        sampleProduct.setSize("10");
        sampleProduct.setRating(5);
    }

    @Test
    @WithMockUser
    void getAll_ShouldReturnList() throws Exception {
        when(productService.findAll()).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Running Shoes"))
                .andExpect(jsonPath("$[0].brand").value("Nike"));
    }

    @Test
    @WithMockUser
    void getById_ShouldReturnProduct() throws Exception {
        when(productService.findById(1)).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Running Shoes"));
    }

    @Test
    @WithMockUser
    void search_ShouldReturnMatches() throws Exception {
        when(productService.searchByName("Shoes")).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products/search").param("name", "Shoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getInventory_ShouldReturnList() throws Exception {
        Inventory inv = new Inventory();
        inv.setInventoryId(101);
        inv.setProductInventory(50);

        when(productService.findById(1)).thenReturn(sampleProduct);
        when(inventoryService.findAllByProductId(1)).thenReturn(List.of(inv));

        mockMvc.perform(get("/api/products/1/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].inventoryId").value(101))
                .andExpect(jsonPath("$[0].productInventory").value(50));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreatedStatus() throws Exception {
        when(productService.save(any(Product.class))).thenReturn(sampleProduct);

        mockMvc.perform(post("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    @WithMockUser
    void update_ShouldReturnUpdatedProduct() throws Exception {
        when(productService.findById(1)).thenReturn(sampleProduct);
        when(productService.save(any(Product.class))).thenReturn(sampleProduct);

        mockMvc.perform(put("/api/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Running Shoes"));
    }

    @Test
    @WithMockUser
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteById(1);

        mockMvc.perform(delete("/api/products/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
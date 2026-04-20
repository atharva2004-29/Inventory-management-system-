package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.model.Store;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShipmentRestController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*Converter"
    )
)
public class ShipmentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShipmentService shipmentService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Shipment sampleShipment;

    @BeforeEach
    void setUp() {
        Store store = new Store();
        store.setStoreId(10);

        Customer customer = new Customer();
        customer.setCustomerId(1);

        sampleShipment = new Shipment();
        sampleShipment.setShipmentId(500);
        sampleShipment.setStore(store);
        sampleShipment.setCustomer(customer);
        sampleShipment.setDeliveryAddress("456 Delivery Lane, Metropolis");
        sampleShipment.setShipmentStatus("IN_TRANSIT");
    }

    @Test
    @WithMockUser
    void getAll_ShouldReturnList() throws Exception {
        when(shipmentService.findAll()).thenReturn(List.of(sampleShipment));

        mockMvc.perform(get("/api/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].shipmentStatus").value("IN_TRANSIT"));
    }

    @Test
    @WithMockUser
    void getById_ShouldReturnShipment() throws Exception {
        when(shipmentService.findById(500)).thenReturn(sampleShipment);

        mockMvc.perform(get("/api/shipments/500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryAddress").value("456 Delivery Lane, Metropolis"));
    }

    @Test
    @WithMockUser
    void getByStatus_ShouldReturnList() throws Exception {
        when(shipmentService.findByStatus("IN_TRANSIT")).thenReturn(List.of(sampleShipment));

        mockMvc.perform(get("/api/shipments/status/IN_TRANSIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shipmentId").value(500));
    }

    @Test
    @WithMockUser
    void getByCustomer_ShouldReturnList() throws Exception {
        when(shipmentService.findByCustomerId(1)).thenReturn(List.of(sampleShipment));

        mockMvc.perform(get("/api/shipments/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer.customerId").value(1));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreatedStatus() throws Exception {
        when(shipmentService.save(any(Shipment.class))).thenReturn(sampleShipment);

        mockMvc.perform(post("/api/shipments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleShipment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shipmentId").value(500));
    }

    @Test
    @WithMockUser
    void update_ShouldReturnUpdatedShipment() throws Exception {
        when(shipmentService.findById(500)).thenReturn(sampleShipment);
        when(shipmentService.save(any(Shipment.class))).thenReturn(sampleShipment);

        mockMvc.perform(put("/api/shipments/500")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleShipment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentStatus").value("IN_TRANSIT"));
    }

    @Test
    @WithMockUser
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(shipmentService).deleteById(500);

        mockMvc.perform(delete("/api/shipments/500")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
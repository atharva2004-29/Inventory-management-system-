package org.example.order_inventory_system.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order_inventory_system.model.*;
import org.example.order_inventory_system.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/view/api")
public class ApiViewController {

    private final CustomerService customerService;
    private final ProductService productService;
    private final StoreService storeService;
    private final OrderService orderService;
    private final ShipmentService shipmentService;
    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    public ApiViewController(CustomerService customerService, ProductService productService, 
                             StoreService storeService, OrderService orderService, 
                             ShipmentService shipmentService, InventoryService inventoryService,
                             ObjectMapper objectMapper) {
        this.customerService = customerService;
        this.productService = productService;
        this.storeService = storeService;
        this.orderService = orderService;
        this.shipmentService = shipmentService;
        this.inventoryService = inventoryService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{entity}")
    public String listEntity(@PathVariable String entity, Model model) {
        Object rawData = switch (entity) {
            case "customers" -> customerService.findAll();
            case "products" -> productService.findAll();
            case "stores" -> storeService.findAll();
            case "orders" -> orderService.findAll();
            case "shipments" -> shipmentService.findAll();
            case "inventory" -> inventoryService.findAll();
            default -> Collections.emptyList();
        };

        model.addAttribute("title", "All " + capitalize(entity));
        model.addAttribute("type", "LIST");
        model.addAttribute("data", convertToMapList(rawData));
        return "api-result";
    }

    @GetMapping("/{entity}/{id}")
    public String getEntityById(@PathVariable String entity, @PathVariable Integer id, Model model) {
        Object rawData = switch (entity) {
            case "customers" -> customerService.findById(id);
            case "products" -> productService.findById(id);
            case "stores" -> storeService.findById(id);
            case "orders" -> orderService.findById(id);
            case "shipments" -> shipmentService.findById(id);
            case "inventory" -> inventoryService.findById(id);
            default -> null;
        };

        model.addAttribute("title", capitalize(entity) + " Details (ID: " + id + ")");
        model.addAttribute("type", "SINGLE");
        model.addAttribute("data", convertToMap(rawData));
        return "api-result";
    }

    @GetMapping("/{entity}/count")
    public String getCount(@PathVariable String entity, Model model) {
        long count = switch (entity) {
            case "customers" -> customerService.findAll().size();
            case "products" -> productService.findAll().size();
            case "stores" -> storeService.findAll().size();
            case "orders" -> orderService.findAll().size();
            case "shipments" -> shipmentService.findAll().size();
            case "inventory" -> inventoryService.findAll().size();
            default -> 0;
        };

        model.addAttribute("title", capitalize(entity) + " Total Count");
        model.addAttribute("type", "COUNT");
        model.addAttribute("data", count);
        return "api-result";
    }

    @GetMapping("/{entity}/export")
    public String export(@PathVariable String entity, Model model) {
        String mockCsv = "Mock data export for " + entity + "\nTimestamp: " + java.time.LocalDateTime.now() + "\nStatus: SUCCESS";
        
        model.addAttribute("title", capitalize(entity) + " Data Export");
        model.addAttribute("type", "RAW");
        model.addAttribute("data", mockCsv);
        return "api-result";
    }
    
    @GetMapping("/{entity}/search")
    public String search(@PathVariable String entity, @RequestParam(defaultValue = "test") String name, Model model) {
        Object rawData = switch (entity) {
            case "customers" -> customerService.searchByName(name);
            case "products" -> productService.searchByName(name);
            case "stores" -> storeService.searchByName(name);
            default -> Collections.emptyList();
        };

        model.addAttribute("title", capitalize(entity) + " Search Results for '" + name + "'");
        model.addAttribute("type", "LIST");
        model.addAttribute("data", convertToMapList(rawData));
        return "api-result";
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private List<Map<String, Object>> convertToMapList(Object obj) {
        try {
            return objectMapper.convertValue(obj, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Map<String, Object> convertToMap(Object obj) {
        if (obj == null) return null;
        
        try {
            // Special handling for Order to flatten customer and store
            if (obj instanceof Order order) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("orderId", order.getOrderId());
                
                if (order.getCustomer() != null) {
                    map.put("customer", order.getCustomer().getFullName() + " (" + order.getCustomer().getEmailAddress() + ")");
                }
                
                if (order.getStore() != null) {
                    map.put("store", order.getStore().getStoreName());
                }
                
                map.put("orderTms", order.getOrderTms());
                map.put("orderStatus", order.getOrderStatus());
                return map;
            }
            
            return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return null;
        }
    }
}

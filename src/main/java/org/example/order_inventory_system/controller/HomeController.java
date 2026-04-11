package org.example.order_inventory_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.service.CustomerService;
import org.example.order_inventory_system.service.InventoryService;
import org.example.order_inventory_system.service.OrderService;
import org.example.order_inventory_system.service.ProductService;
import org.example.order_inventory_system.service.ShipmentService;
import org.example.order_inventory_system.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CustomerService customerService;
    private final ProductService productService;
    private final StoreService storeService;
    private final OrderService orderService;
    private final InventoryService inventoryService;
    private final ShipmentService shipmentService;

    @GetMapping("/")
    public String home(Model model) {
//        model.addAttribute("totalCustomers",  customerService.findAll().size());
//        model.addAttribute("totalProducts",   productService.findAll().size());
//        model.addAttribute("totalStores",     storeService.findAll().size());
//        model.addAttribute("totalOrders",     orderService.findAll().size());
//        model.addAttribute("totalInventory",  inventoryService.findAll().size());
//        model.addAttribute("totalShipments",  shipmentService.findAll().size());
        return "home";
    }
}
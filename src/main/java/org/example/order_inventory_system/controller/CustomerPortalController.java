package org.example.order_inventory_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.repository.CustomerRepository;
import org.example.order_inventory_system.service.OrderService;
import org.example.order_inventory_system.service.ProductService;
import org.example.order_inventory_system.service.ShipmentService;
import org.example.order_inventory_system.service.StoreService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerPortalController {

    private final ProductService productService;
    private final OrderService orderService;
    private final ShipmentService shipmentService;
    private final CustomerRepository customerRepository;
    private final StoreService storeService; // ADD THIS INJECTION


    private Customer getLoggedInCustomer() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return customerRepository.findByEmailAddress(email).orElseThrow();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("customer", getLoggedInCustomer());
        return "customer/dashboard";
    }

    @GetMapping("/products")
    public String browseProducts(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("products", productService.searchByName(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("products", productService.findAll());
        }
        return "customer/products";
    }

    @GetMapping("/orders")
    public String myOrders(Model model) {
        Customer customer = getLoggedInCustomer();
        model.addAttribute("orders", orderService.findByCustomerId(
                customer.getCustomerId()));
        return "customer/orders";
    }

    @GetMapping("/shipments")
    public String myShipments(Model model) {
        Customer customer = getLoggedInCustomer();
        model.addAttribute("shipments", shipmentService.findByCustomerId(
                customer.getCustomerId()));
        return "customer/shipments";
    }

    // Show order form
    @GetMapping("/order/new")
    public String showOrderForm(@RequestParam Integer productId, Model model) {
        model.addAttribute("product", productService.findById(productId));
        model.addAttribute("stores", storeService.findAll());
        model.addAttribute("customer", getLoggedInCustomer());
        return "customer/order-form";
    }

    // Submit order
    @PostMapping("/order/place")
    public String placeOrder(@RequestParam Integer productId,
                             @RequestParam Integer storeId,
                             RedirectAttributes redirectAttributes) {
        Customer customer = getLoggedInCustomer();
        try {
            orderService.placeOrder(customer.getCustomerId(), storeId, productId);
            redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customer/order/new?productId=" + productId;
        }
        return "redirect:/customer/orders";
    }
}
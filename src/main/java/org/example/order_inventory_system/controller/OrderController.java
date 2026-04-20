package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.service.CustomerService;
import org.example.order_inventory_system.service.OrderService;
import org.example.order_inventory_system.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final StoreService storeService;

    public OrderController(OrderService orderService, CustomerService customerService, StoreService storeService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.storeService = storeService;
    }

    // List all / filter by status
    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        if (status != null && !status.isBlank()) {
            model.addAttribute("orders", orderService.findByStatus(status));
            model.addAttribute("selectedStatus", status);
        } else {
            model.addAttribute("orders", orderService.findAll());
        }
        return "orders/list";
    }

    // Show add form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("formTitle", "Add Order");
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("stores", storeService.findAll());
        return "orders/form";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        model.addAttribute("formTitle", "Edit Order");
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("stores", storeService.findAll());
        return "orders/form";
    }

    // Save (add or update)
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Order order,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle",
                    order.getOrderId() == null ? "Add Order" : "Edit Order");
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("stores", storeService.findAll());
            return "orders/form";
        }
        orderService.save(order);
        redirectAttributes.addFlashAttribute("successMessage", "Order saved successfully!");
        return "redirect:/orders";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        orderService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully!");
        return "redirect:/orders";
    }
}
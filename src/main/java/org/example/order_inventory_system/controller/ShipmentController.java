package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.service.CustomerService;
import org.example.order_inventory_system.service.ShipmentService;
import org.example.order_inventory_system.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final StoreService storeService;
    private final CustomerService customerService;

    // List all / filter by status
    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        if (status != null && !status.isBlank()) {
            model.addAttribute("shipments", shipmentService.findByStatus(status));
            model.addAttribute("selectedStatus", status);
        } else {
            model.addAttribute("shipments", shipmentService.findAll());
        }
        return "shipments/list";
    }

    // Show add form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("shipment", new Shipment());
        model.addAttribute("formTitle", "Add Shipment");
        model.addAttribute("stores", storeService.findAll());
        model.addAttribute("customers", customerService.findAll());
        return "shipments/form";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("shipment", shipmentService.findById(id));
        model.addAttribute("formTitle", "Edit Shipment");
        model.addAttribute("stores", storeService.findAll());
        model.addAttribute("customers", customerService.findAll());
        return "shipments/form";
    }

    // Save (add or update)
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Shipment shipment,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle",
                    shipment.getShipmentId() == null ? "Add Shipment" : "Edit Shipment");
            model.addAttribute("stores", storeService.findAll());
            model.addAttribute("customers", customerService.findAll());
            return "shipments/form";
        }
        shipmentService.save(shipment);
        redirectAttributes.addFlashAttribute("successMessage", "Shipment saved successfully!");
        return "redirect:/shipments";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        shipmentService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Shipment deleted successfully!");
        return "redirect:/shipments";
    }
}
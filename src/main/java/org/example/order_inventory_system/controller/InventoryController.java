package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.service.InventoryService;
import org.example.order_inventory_system.service.ProductService;
import org.example.order_inventory_system.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class  InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;
    private final StoreService storeService;

    // List all / filter by low stock
    @GetMapping
    public String list(@RequestParam(required = false) Integer threshold, Model model) {
        if (threshold != null) {
            model.addAttribute("inventories", inventoryService.findLowStock(threshold));
            model.addAttribute("threshold", threshold);
        } else {
            model.addAttribute("inventories", inventoryService.findAll());
        }
        return "inventory/list";
    }

    // Show add form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        model.addAttribute("formTitle", "Add Inventory");
        model.addAttribute("products", productService.findAll());
        model.addAttribute("stores", storeService.findAll());
        return "inventory/form";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("inventory", inventoryService.findById(id));
        model.addAttribute("formTitle", "Edit Inventory");
        model.addAttribute("products", productService.findAll());
        model.addAttribute("stores", storeService.findAll());
        return "inventory/form";
    }

    // Save (add or update)
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Inventory inventory,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle",
                    inventory.getInventoryId() == null ? "Add Inventory" : "Edit Inventory");
            model.addAttribute("products", productService.findAll());
            model.addAttribute("stores", storeService.findAll());
            return "inventory/form";
        }
        inventoryService.save(inventory);
        redirectAttributes.addFlashAttribute("successMessage", "Inventory saved successfully!");
        return "redirect:/inventory";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        inventoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Inventory deleted successfully!");
        return "redirect:/inventory";
    }
}
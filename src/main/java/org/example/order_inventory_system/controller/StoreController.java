package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // List all / search
    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("stores", storeService.searchByName(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("stores", storeService.findAll());
        }
        return "stores/list";
    }

    // Show add form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("store", new Store());
        model.addAttribute("formTitle", "Add Store");
        return "stores/form";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("store", storeService.findById(id));
        model.addAttribute("formTitle", "Edit Store");
        return "stores/form";
    }

    // Save (add or update)
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Store store,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle",
                    store.getStoreId() == null ? "Add Store" : "Edit Store");
            return "stores/form";
        }
        storeService.save(store);
        redirectAttributes.addFlashAttribute("successMessage", "Store saved successfully!");
        return "redirect:/stores";
    }

//    // Delete
//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
//
//        redirectAttributes.addFlashAttribute("successMessage", "Store deleted successfully!");
//        return "redirect:/stores";
//    }
}

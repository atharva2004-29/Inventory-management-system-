
package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // List all / search
    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("customers", customerService.searchByName(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("customers", customerService.findAll());
        }
        return "customers/list";
    }

    // Show add form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("formTitle", "Add Customer");
        return "customers/form";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        model.addAttribute("formTitle", "Edit Customer");
        return "customers/form";
    }

    // Save (add or update)
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Customer customer,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle",
                    customer.getCustomerId() == null ? "Add Customer" : "Edit Customer");
            return "customers/form";
        }
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Customer saved successfully!");
        return "redirect:/customers";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        customerService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Customer deleted successfully!");
        return "redirect:/customers";
    }
}
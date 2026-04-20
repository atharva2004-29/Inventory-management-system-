package org.example.order_inventory_system.controller;

import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private CustomerController customerController;

    // ✅ Test: list without search
    @Test
    void testListWithoutSearch() {
        List<Customer> customers = List.of(new Customer(), new Customer());
        when(customerService.findAll()).thenReturn(customers);

        String view = customerController.list(null, model);

        assertEquals("customers/list", view);
        verify(model).addAttribute("customers", customers);
    }

    // ✅ Test: list with search
    @Test
    void testListWithSearch() {
        String search = "john";
        List<Customer> customers = List.of(new Customer());
        when(customerService.searchByName(search)).thenReturn(customers);

        String view = customerController.list(search, model);

        assertEquals("customers/list", view);
        verify(model).addAttribute("customers", customers);
        verify(model).addAttribute("search", search);
    }

    // ✅ Test: show add form
    @Test
    void testShowAddForm() {
        String view = customerController.showAddForm(model);

        assertEquals("customers/form", view);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
        verify(model).addAttribute("formTitle", "Add Customer");
    }

    // ✅ Test: show edit form
    @Test
    void testShowEditForm() {
        Customer customer = new Customer();
        when(customerService.findById(1)).thenReturn(customer);

        String view = customerController.showEditForm(1, model);

        assertEquals("customers/form", view);
        verify(model).addAttribute("customer", customer);
        verify(model).addAttribute("formTitle", "Edit Customer");
    }

    // ✅ Test: save with validation errors
    @Test
    void testSaveWithErrors() {
        Customer customer = new Customer();
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = customerController.save(customer, bindingResult, model, redirectAttributes);

        assertEquals("customers/form", view);
        verify(customerService, never()).save(any());
    }

    // ✅ Test: save success
    @Test
    void testSaveSuccess() {
        Customer customer = new Customer();
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = customerController.save(customer, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/customers", view);
        verify(customerService).save(customer);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Customer saved successfully!");
    }

    // ✅ Test: delete
    @Test
    void testDelete() {
        String view = customerController.delete(1, redirectAttributes);

        assertEquals("redirect:/customers", view);
        verify(customerService).deleteById(1);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Customer deleted successfully!");
    }
}
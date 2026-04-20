package org.example.order_inventory_system.controller;

import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.service.CustomerService;
import org.example.order_inventory_system.service.OrderService;
import org.example.order_inventory_system.service.StoreService;
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
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerService customerService;

    @Mock
    private StoreService storeService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private OrderController orderController;

    // ✅ Test: list without status
    @Test
    void testListWithoutStatus() {
        List<Order> orders = List.of(new Order(), new Order());
        when(orderService.findAll()).thenReturn(orders);

        String view = orderController.list(null, model);

        assertEquals("orders/list", view);
        verify(model).addAttribute("orders", orders);
    }

    // ✅ Test: list with status
    @Test
    void testListWithStatus() {
        String status = "SHIPPED";
        List<Order> orders = List.of(new Order());
        when(orderService.findByStatus(status)).thenReturn(orders);

        String view = orderController.list(status, model);

        assertEquals("orders/list", view);
        verify(model).addAttribute("orders", orders);
        verify(model).addAttribute("selectedStatus", status);
    }

    // ✅ Test: show add form
    @Test
    void testShowAddForm() {
        when(customerService.findAll()).thenReturn(List.of());
        when(storeService.findAll()).thenReturn(List.of());

        String view = orderController.showAddForm(model);

        assertEquals("orders/form", view);
        verify(model).addAttribute(eq("order"), any(Order.class));
        verify(model).addAttribute("formTitle", "Add Order");
        verify(model).addAttribute("customers", List.of());
        verify(model).addAttribute("stores", List.of());
    }

    // ✅ Test: show edit form
    @Test
    void testShowEditForm() {
        Order order = new Order();
        when(orderService.findById(1)).thenReturn(order);
        when(customerService.findAll()).thenReturn(List.of());
        when(storeService.findAll()).thenReturn(List.of());

        String view = orderController.showEditForm(1, model);

        assertEquals("orders/form", view);
        verify(model).addAttribute("order", order);
        verify(model).addAttribute("formTitle", "Edit Order");
        verify(model).addAttribute("customers", List.of());
        verify(model).addAttribute("stores", List.of());
    }

    // ✅ Test: save with validation errors
    @Test
    void testSaveWithErrors() {
        Order order = new Order();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(customerService.findAll()).thenReturn(List.of());
        when(storeService.findAll()).thenReturn(List.of());

        String view = orderController.save(order, bindingResult, model, redirectAttributes);

        assertEquals("orders/form", view);
        verify(orderService, never()).save(any());
        verify(model).addAttribute("customers", List.of());
        verify(model).addAttribute("stores", List.of());
    }

    // ✅ Test: save success
    @Test
    void testSaveSuccess() {
        Order order = new Order();
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = orderController.save(order, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/orders", view);
        verify(orderService).save(order);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Order saved successfully!");
    }

    // ✅ Test: delete
    @Test
    void testDelete() {
        String view = orderController.delete(1, redirectAttributes);

        assertEquals("redirect:/orders", view);
        verify(orderService).deleteById(1);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Order deleted successfully!");
    }
}
package org.example.order_inventory_system.controller;

import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.service.ProductService;
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
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ProductController productController;

    // ✅ Test: list without search
    @Test
    void testListWithoutSearch() {
        List<Product> products = List.of(new Product(), new Product());
        when(productService.findAll()).thenReturn(products);

        String view = productController.list(null, model);

        assertEquals("products/list", view);
        verify(model).addAttribute("products", products);
    }

    // ✅ Test: list with search
    @Test
    void testListWithSearch() {
        String search = "laptop";
        List<Product> products = List.of(new Product());
        when(productService.searchByName(search)).thenReturn(products);

        String view = productController.list(search, model);

        assertEquals("products/list", view);
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("search", search);
    }

    // ✅ Test: list with empty search (edge case)
    @Test
    void testListWithEmptySearch() {
        when(productService.findAll()).thenReturn(List.of());

        String view = productController.list("", model);

        assertEquals("products/list", view);
        verify(productService).findAll();
    }

    // ✅ Test: show add form
    @Test
    void testShowAddForm() {
        String view = productController.showAddForm(model);

        assertEquals("products/form", view);
        verify(model).addAttribute(eq("product"), any(Product.class));
        verify(model).addAttribute("formTitle", "Add Product");
    }

    // ✅ Test: show edit form
    @Test
    void testShowEditForm() {
        Product product = new Product();
        when(productService.findById(1)).thenReturn(product);

        String view = productController.showEditForm(1, model);

        assertEquals("products/form", view);
        verify(model).addAttribute("product", product);
        verify(model).addAttribute("formTitle", "Edit Product");
    }

    // ✅ Test: save with validation error (Add Product)
    @Test
    void testSaveError_AddProduct() {
        Product product = new Product(); // productId = null

        when(bindingResult.hasErrors()).thenReturn(true);

        String view = productController.save(product, bindingResult, model, redirectAttributes);

        assertEquals("products/form", view);
        verify(model).addAttribute("formTitle", "Add Product");
        verify(productService, never()).save(any());
    }

    // ✅ Test: save with validation error (Edit Product)
    @Test
    void testSaveError_EditProduct() {
        Product product = new Product();
        product.setProductId(1);

        when(bindingResult.hasErrors()).thenReturn(true);

        String view = productController.save(product, bindingResult, model, redirectAttributes);

        assertEquals("products/form", view);
        verify(model).addAttribute("formTitle", "Edit Product");
        verify(productService, never()).save(any());
    }

    // ✅ Test: save success
    @Test
    void testSaveSuccess() {
        Product product = new Product();

        when(bindingResult.hasErrors()).thenReturn(false);

        String view = productController.save(product, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/products", view);
        verify(productService).save(product);
        verify(redirectAttributes)
                .addFlashAttribute("successMessage", "Product saved successfully!");
    }

    // ✅ Test: delete
    @Test
    void testDelete() {
        String view = productController.delete(1, redirectAttributes);

        assertEquals("redirect:/products", view);
        verify(productService).deleteById(1);
        verify(redirectAttributes)
                .addFlashAttribute("successMessage", "Product deleted successfully!");
    }
}
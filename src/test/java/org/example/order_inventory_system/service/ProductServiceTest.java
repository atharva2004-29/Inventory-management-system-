package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.Product;
import org.example.order_inventory_system.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(1);
        product.setProductName("Slim Fit Jeans");
        product.setBrand("Levi's");
        product.setColour("Blue");
        product.setSize("M");
        product.setUnitPrice(new BigDecimal("1999.99"));
        product.setRating(4);
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.findAll();

        assertEquals(1, result.size());
        assertEquals("Slim Fit Jeans", result.get(0).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product result = productService.findById(1);

        assertNotNull(result);
        assertEquals("Levi's", result.getBrand());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productService.findById(99));

        assertEquals("Product not found: 99", ex.getMessage());
    }

    @Test
    void save_ShouldReturnSavedProduct() {
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.save(product);

        assertNotNull(result);
        assertEquals("Slim Fit Jeans", result.getProductName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(productRepository).deleteById(1);

        productService.deleteById(1);

        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void searchByName_ShouldReturnMatchingProducts() {
        when(productRepository.findByProductNameContainingIgnoreCase("jeans"))
                .thenReturn(List.of(product));

        List<Product> result = productService.searchByName("jeans");

        assertEquals(1, result.size());
        assertEquals("Slim Fit Jeans", result.get(0).getProductName());
    }
}

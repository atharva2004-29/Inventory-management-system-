package org.example.order_inventory_system.service;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setFullName("John Doe");
        customer.setEmailAddress("john@example.com");
    }

    @Test
    void findAll_ShouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.findAll();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnCustomer_WhenExists() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        Customer result = customerService.findById(1);

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmailAddress());
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> customerService.findById(99));

        assertEquals("Customer not found: 99", ex.getMessage());
    }

    @Test
    void save_ShouldReturnSavedCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.save(customer);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(customerRepository).deleteById(1);

        customerService.deleteById(1);

        verify(customerRepository, times(1)).deleteById(1);
    }
    @Test
    void searchByName_ShouldReturnMatchingCustomers() {
        when(customerRepository.findByFullNameContainingIgnoreCase("john"))
                .thenReturn(List.of(customer));

        List<Customer> result = customerService.searchByName("john");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
    }
}
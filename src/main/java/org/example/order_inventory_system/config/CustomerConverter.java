package org.example.order_inventory_system.config;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.repository.CustomerRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerConverter implements Converter<String, Customer> {

    private final CustomerRepository customerRepository;

    @Override
    public Customer convert(String id) {
        if (id == null || id.isBlank()) return null;
        return customerRepository.findById(Integer.parseInt(id)).orElse(null);
    }
}
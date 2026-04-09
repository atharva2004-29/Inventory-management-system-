package org.example.order_inventory_system.repository;

import org.example.order_inventory_system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByFullNameContainingIgnoreCase(String name);
}
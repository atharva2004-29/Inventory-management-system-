package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.Role;
import org.example.order_inventory_system.model.User;
import org.example.order_inventory_system.repository.CustomerRepository;
import org.example.order_inventory_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerCustomer(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode("NO_PASSWORD")); // dummy password
        user.setRole(Role.ROLE_CUSTOMER);

        Customer customer = new Customer();
        customer.setFullName(user.getFullName());
        customer.setEmailAddress(user.getEmail());
        Customer savedCustomer = customerRepository.save(customer);

        user.setCustomer(savedCustomer);
        userRepository.save(user);
    }

    public void createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(">>> Encoded password: " + user.getPassword()); // ADD THIS
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
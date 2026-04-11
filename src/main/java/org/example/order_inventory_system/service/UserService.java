package org.example.order_inventory_system.service;

import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.Role;
import org.example.order_inventory_system.model.User;
import org.example.order_inventory_system.repository.CustomerRepository;
import org.example.order_inventory_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerCustomer(User user) {
        // check email not already taken
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }

        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_CUSTOMER);

        // create matching Customer record
        Customer customer = new Customer();
        customer.setFullName(user.getFullName());
        customer.setEmailAddress(user.getEmail());
        Customer savedCustomer = customerRepository.save(customer);

        // link user to customer
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
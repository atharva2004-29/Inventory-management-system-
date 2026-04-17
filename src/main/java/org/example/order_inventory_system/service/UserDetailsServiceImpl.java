package org.example.order_inventory_system.service;

import org.example.order_inventory_system.model.User;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.repository.CustomerRepository;
import org.example.order_inventory_system.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // check users table first (admin)
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority(user.getRole().name()))
            );
        }

        // check customers table (email-only login)
        Customer customer = customerRepository.findByEmailAddress(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No account found: " + email));

        return new org.springframework.security.core.userdetails.User(
                customer.getEmailAddress(),
                "",
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );
    }
}
package org.example.order_inventory_system.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Customer;
import org.example.order_inventory_system.model.User;
import org.example.order_inventory_system.repository.CustomerRepository;
import org.example.order_inventory_system.repository.UserRepository;
import org.example.order_inventory_system.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("errorMessage", "Email not found.");
        if (logout != null) model.addAttribute("successMessage", "Logged out successfully.");
        return "auth/login";
    }

    @GetMapping("/admin-login")
    public String adminLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("errorMessage", "Invalid email or password.");
        return "auth/admin-login";
    }

    @PostMapping("/customer-login")
    public String customerLogin(@RequestParam String email,
                                Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        Customer customer = customerRepository.findByEmailAddress(email).orElse(null);

        if (customer == null) {
            model.addAttribute("errorMessage", "No account found with that email.");
            return "auth/login";
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                customer.getEmailAddress(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        new HttpSessionSecurityContextRepository()
                .saveContext(SecurityContextHolder.getContext(), request, response);

        return "redirect:/customer/dashboard";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    @GetMapping("/hash-test")
    @ResponseBody
    public String hashTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode("admin123");
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "auth/register";
        try {
            userService.registerCustomer(user);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}
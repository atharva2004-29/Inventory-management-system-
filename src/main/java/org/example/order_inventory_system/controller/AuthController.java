package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.User;
import org.example.order_inventory_system.repository.UserRepository;
import org.example.order_inventory_system.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;  // ADD THIS


    // Login page
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Logged out successfully.");
        }
        return "auth/login";
    }

    // Register page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // Register submit
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }
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

//    @GetMapping("/init-admin")
//    public String initAdmin() {
//        // delete existing admin first
//        User existing = userRepository.findByEmail("admin@system.com").orElse(null);
//        if (existing != null) {
//            userRepository.delete(existing);
//        }
//
//        User admin = new User();
//        admin.setFullName("Admin");
//        admin.setEmail("admin@system.com");
//        admin.setPassword("admin123");
//        userService.createAdmin(admin);
//        return "redirect:/login";
//    }

    @GetMapping("/hash-test")
    @ResponseBody
    public String hashTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("admin123");
        System.out.println(">>> HASH: " + hash);
        return hash;
    }


}
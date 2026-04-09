package org.example.order_inventory_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;
}

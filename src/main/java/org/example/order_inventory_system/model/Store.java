package org.example.order_inventory_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Integer storeId;

    @NotBlank(message = "Store name is required")
    @Column(name = "store_name", nullable = false, unique = true)
    private String storeName;

    @Column(name = "web_address")
    private String webAddress;

    @Column(name = "physical_address")
    private String physicalAddress;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;
}
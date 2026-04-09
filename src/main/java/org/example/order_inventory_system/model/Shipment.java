package org.example.order_inventory_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Integer shipmentId;

//    @NotNull(message = "Order is required")
//    @ManyToOne
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;

    @NotNull(message = "Store is required")
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @NotNull(message = "Customer is required")
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "shipment_status")
    private String shipmentStatus;
}
package org.example.order_inventory_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "line_item_id", nullable = false)
    private Integer lineItemId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @MapsId  // tells JPA that order_id is both the PK and the FK to orders
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @NotNull
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @Min(1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // Manual No-Args Constructor
    public OrderItem() {}

    // Getters and Setters
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getLineItemId() { return lineItemId; }
    public void setLineItemId(Integer lineItemId) { this.lineItemId = lineItemId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Shipment getShipment() { return shipment; }
    public void setShipment(Shipment shipment) { this.shipment = shipment; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
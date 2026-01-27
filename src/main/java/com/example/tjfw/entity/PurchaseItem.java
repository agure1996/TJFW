package com.example.tjfw.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_items")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant product_variant;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    protected PurchaseItem() {
    }

    public PurchaseItem(Purchase purchase, ProductVariant productVariant, int quantity, BigDecimal costPrice) {
        this.purchase = purchase;
        this.product_variant = productVariant;
        this.quantity = quantity;
        this.costPrice = costPrice;
    }

    public Long getId() {
        return id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public ProductVariant getProductVariant() {
        return product_variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", costPrice=" + costPrice +
                '}';
    }

}

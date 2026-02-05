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
    private ProductVariant productVariant;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    public PurchaseItem() {
    }

    public PurchaseItem(Purchase purchase, ProductVariant productVariant, int quantity, BigDecimal costPrice) {
        this.purchase = purchase;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.costPrice = costPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
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

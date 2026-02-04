package com.example.tjfw.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private String color;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice = BigDecimal.ZERO;
    private int size;
    private int quantity;
    @Column(unique = true, nullable = false)
    private String sku;
    protected ProductVariant() {}
    public ProductVariant(Product product, String color, int size, int quantity, BigDecimal costPrice) {
        this.product = product;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
        this.salePrice = costPrice;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Long getProductVariantId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //removed product from toString since it lazy loads
    @Override
    public String toString() {
        return "ProductVariant{" +
                "id=" + id +
                ", color='" + color + '\'' +
                ", size=" + size +
                ", quantity=" + quantity +
                ", sale price=" + salePrice +
                '}';
    }
}

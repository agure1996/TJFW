package com.example.tjfw.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    private String productDescription;

    protected Product() {
    }

    public Product(String productName, ProductType productType, String productDescription) {
        this.productName = productName;
        this.productType = productType;
        this.productDescription = productDescription;
    }

    public Long getProductId() {
        return id;
    }

    public void setProductId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getDescription() {
        return productDescription;
    }

    public void setDescription(String description) {
        productDescription = description;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productType=" + productType +
                ", Description='" + productDescription + '\'' +
                '}';
    }


}

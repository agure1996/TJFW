package com.example.tjfw.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String product_name;
    @Enumerated(EnumType.STRING)
    private ProductType product_type;
    private String product_description;

    protected Product() {
    }

    public Product(String product_name, ProductType product_type, String product_description) {
        this.product_name = product_name;
        this.product_type = product_type;
        this.product_description = product_description;
    }

    public Long getProductId() {
        return id;
    }

    public void setProductId(Long id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public ProductType getProduct_type() {
        return product_type;
    }

    public void setProduct_type(ProductType product_type) {
        this.product_type = product_type;
    }

    public String getDescription() {
        return product_description;
    }

    public void setDescription(String description) {
        product_description = description;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", product_type=" + product_type +
                ", Description='" + product_description + '\'' +
                '}';
    }


}

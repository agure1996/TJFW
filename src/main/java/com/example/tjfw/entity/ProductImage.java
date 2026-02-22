package com.example.tjfw.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    // 🔹 Product can now be nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    // 🔹 NEW: Variant relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = true)
    private ProductVariant variant;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private String publicId;

    @Column
    private String altText;

    @Column(nullable = false)
    private Boolean isMain = false;

    @Column
    private Integer displayOrder = 0;


    // No-argument constructor
    public ProductImage() {}

    public ProductImage(Product product, ProductVariant variant, String imageUrl, String publicId, Boolean isMain, Integer displayOrder, String altText) {
        this.product = product;
        this.variant = variant;
        this.imageUrl = imageUrl;
        this.publicId = publicId;
        this.isMain = isMain != null ? isMain : false;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.altText = altText;
    }

    // Getters and Setters
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariant getProductVariant() {
        return variant;
    }

    public void setProductVariant(ProductVariant variant) {
        this.variant = variant;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }
}
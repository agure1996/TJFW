package com.example.tjfw.dto.product;

import com.example.tjfw.entity.ProductType;

//DTO for product Requests
public class RequestProductDTO {

    private String productName;
    private ProductType productType;
    private String productDescription;
    private Long supplierId;

    public RequestProductDTO(){}
    
    public RequestProductDTO(String productName, ProductType productType, String productDescription) {
        this.productName = productName;
        this.productType = productType;
        this.productDescription = productDescription;
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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}

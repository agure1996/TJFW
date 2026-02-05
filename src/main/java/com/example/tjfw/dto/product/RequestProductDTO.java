package com.example.tjfw.dto.product;

//DTO for product Requests
public class RequestProductDTO {

    private String productName;
    private String productType;
    private String productDescription;

    public RequestProductDTO(String productName, String productType, String productDescription) {
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

}

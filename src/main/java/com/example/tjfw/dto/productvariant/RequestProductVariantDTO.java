package com.example.tjfw.dto.productvariant;

import java.math.BigDecimal;

public class RequestProductVariantDTO {

    private Long productId;
    private String color;
    private BigDecimal salePrice;
    private int size;
    private int quantity;

    public RequestProductVariantDTO() {}

    public RequestProductVariantDTO(Long productId, String color, BigDecimal salePrice, int size, int quantity) {
        this.productId = productId;
        this.color = color;
        this.salePrice = salePrice;
        this.size = size;
        this.quantity = quantity;
    }

    // getters and setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

package com.example.tjfw.dto.productvariant;

import java.math.BigDecimal;

public class RequestProductVariantDTO {


    private String color;
    private BigDecimal salePrice;
    private int size;
    private int quantity;

    public RequestProductVariantDTO() {}

    public RequestProductVariantDTO( String color, BigDecimal salePrice, int size, int quantity) {
        this.color = color;
        this.salePrice = salePrice;
        this.size = size;
        this.quantity = quantity;
    }

    // getters and setters
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

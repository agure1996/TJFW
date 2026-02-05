package com.example.tjfw.dto.saleitem;

import java.math.BigDecimal;

public class RequestSaleItemDTO {
    private Long productVariantId;
    private int quantity;
    private BigDecimal salePrice;

    public RequestSaleItemDTO() {}

    public RequestSaleItemDTO(Long productVariantId, int quantity, BigDecimal salePrice) {
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

    public Long getProductVariantId() { return productVariantId; }
    public void setProductVariantId(Long productVariantId) { this.productVariantId = productVariantId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
}

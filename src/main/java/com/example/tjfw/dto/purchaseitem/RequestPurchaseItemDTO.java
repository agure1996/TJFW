package com.example.tjfw.dto.purchaseitem;

import java.math.BigDecimal;

public class RequestPurchaseItemDTO {

    private Long purchaseId;
    private Long productVariantId;
    private int quantity;
    private BigDecimal costPrice;

    public RequestPurchaseItemDTO() {}

    public RequestPurchaseItemDTO(Long purchaseId, Long productVariantId, int quantity, BigDecimal costPrice) {
        this.purchaseId = purchaseId;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.costPrice = costPrice;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Long getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(Long productVariantId) {
        this.productVariantId = productVariantId;
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
}

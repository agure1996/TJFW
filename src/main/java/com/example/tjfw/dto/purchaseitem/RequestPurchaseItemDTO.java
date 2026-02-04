package com.example.tjfw.dto.purchaseitem;

import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.entity.Purchase;

import java.math.BigDecimal;

public class RequestPurchaseItemDTO {

    private Purchase purchase;
    private ProductVariant productVariant;
    private int quantity;
    private BigDecimal costPrice;

    public RequestPurchaseItemDTO(Purchase purchase, ProductVariant productVariant, int quantity, BigDecimal costPrice) {
        this.purchase = purchase;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.costPrice = costPrice;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
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

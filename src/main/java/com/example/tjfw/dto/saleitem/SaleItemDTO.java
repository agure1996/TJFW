package com.example.tjfw.dto.saleitem;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import java.math.BigDecimal;

public class SaleItemDTO {
    private ProductVariantDTO productVariant;
    private int quantity;
    private BigDecimal salePrice;

    public SaleItemDTO(ProductVariantDTO productVariant, int quantity, BigDecimal salePrice) {
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

    public ProductVariantDTO getProductVariant() { return productVariant; }
    public int getQuantity() { return quantity; }
    public BigDecimal getSalePrice() { return salePrice; }
}

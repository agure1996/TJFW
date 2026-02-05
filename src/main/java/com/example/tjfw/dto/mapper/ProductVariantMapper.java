package com.example.tjfw.dto.mapper;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.entity.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantMapper {
    public ProductVariantDTO toDTO(ProductVariant variant) {
        return new ProductVariantDTO(
                variant.getProductVariantId(),
                variant.getProduct().getProductId(),
                variant.getProduct().getProductName(),
                variant.getColor(),
                variant.getSalePrice(),
                variant.getSize(),
                variant.getQuantity(),
                variant.getSku()
        );
    }
}

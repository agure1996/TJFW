package com.example.tjfw.dto.productvariant;

import java.math.BigDecimal;

public record ProductVariantDTO(Long productVariantId, Long productId, String productName, String color,
                                BigDecimal salePrice, int size, int quantity, String sku) {
}

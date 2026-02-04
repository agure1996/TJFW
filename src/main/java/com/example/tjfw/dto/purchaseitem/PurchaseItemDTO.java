package com.example.tjfw.dto.purchaseitem;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import java.math.BigDecimal;

public record PurchaseItemDTO(Long purchaseItemId, ProductVariantDTO productVariant, int quantity, BigDecimal costPrice) {}
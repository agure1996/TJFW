package com.example.tjfw.dto.purchaseitem;

import com.example.tjfw.entity.ProductVariant;

import java.math.BigDecimal;

public record PurchaseItemDTO(Long purchaseItemId, ProductVariant productVariant, int quantity, BigDecimal costPrice) {}

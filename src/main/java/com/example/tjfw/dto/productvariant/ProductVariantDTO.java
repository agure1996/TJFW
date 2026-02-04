package com.example.tjfw.dto.productvariant;

import com.example.tjfw.entity.Product;

import java.math.BigDecimal;

public record ProductVariantDTO(Long productVariantId, Product product, String color, BigDecimal salePrice, int size, int quantity) {}

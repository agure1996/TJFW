package com.example.tjfw.dto.product;

import com.example.tjfw.entity.ProductType;

//API Response DTO for product
public record ProductDTO(Long productId, String productName, ProductType productType, String productDescription, Long supplierId) {}

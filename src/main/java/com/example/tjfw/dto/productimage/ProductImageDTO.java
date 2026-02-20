package com.example.tjfw.dto.productimage;
public record ProductImageDTO(
        Long imageId,
        String imageUrl,
        String publicId,
        Boolean isMain,
        Integer displayOrder,
        String altText
) {}
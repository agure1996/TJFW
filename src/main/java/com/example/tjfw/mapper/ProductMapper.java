package com.example.tjfw.mapper;

import com.example.tjfw.dto.product.ProductDTO;
import com.example.tjfw.dto.productimage.ProductImageDTO;
import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductImage;
import com.example.tjfw.entity.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getProductType(),
                product.getProductDescription(),
                product.getSupplier() != null ? product.getSupplier().getSupplierId() : null
        );
    }

    public ProductVariantDTO toVariantDTO(ProductVariant variant) {
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

    public ProductImageDTO toImageDTO(ProductImage image) {
        return new ProductImageDTO(
                image.getImageId(),
                image.getImageUrl(),
                image.getPublicId(),
                image.getIsMain(),
                image.getDisplayOrder(),
                image.getAltText(),
                image.getProduct() != null ? image.getProduct().getProductId() : null,
                image.getProductVariant() != null ? image.getProductVariant().getProductVariantId() : null
        );
    }
}

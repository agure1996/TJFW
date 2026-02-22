package com.example.tjfw.service;

import com.example.tjfw.mapper.ProductMapper;
import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.exceptions.AlreadyExistsException;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductMapper productMapper;

    public ProductVariantService(ProductVariantRepository productVariantRepository,
                                 ProductMapper productMapper) {
        this.productVariantRepository = productVariantRepository;
        this.productMapper = productMapper;
    }

    private void checkVariantUniqueness(ProductVariant productVariant) {
        boolean variantExists = productVariantRepository.existsByProductAndColorAndSize(
                productVariant.getProduct(), productVariant.getColor(), productVariant.getSize());
        if (variantExists) {
            throw new AlreadyExistsException("Product variant already exists");
        }
    }

    private String safePrefix(String value) {
        return value.length() >= 3
                ? value.substring(0, 3).toUpperCase()
                : value.toUpperCase();
    }

    private String generateSku(ProductVariant v) {
        String sku;
        do {
            sku = String.format(
                    "%s-%s-%s-%d",
                    safePrefix(v.getProduct().getProductName()),
                    safePrefix(v.getColor()),
                    v.getSize(),
                    System.currentTimeMillis() % 10000
            );
        } while (productVariantRepository.existsBySku(sku));
        return sku;
    }

    private ProductVariant getProductVariantOrThrow(Long id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product variant not found"));
    }

    public ProductVariant findProductVariantById(Long id) {
        return getProductVariantOrThrow(id);
    }

    public List<ProductVariant> findAllByProductId(Long productId) {
        return productVariantRepository.findAllVariantsByProductId(productId);
    }

    public List<ProductVariantDTO> findAllProductVariantsDTO() {
        return productVariantRepository.findAll().stream()
                .map(productMapper::toVariantDTO)
                .toList();
    }

    public ProductVariant createNewProductVariant(ProductVariant productVariant) {
        checkVariantUniqueness(productVariant);
        productVariant.setSku(generateSku(productVariant));
        return productVariantRepository.save(productVariant);
    }

    public ProductVariant updateProductVariant(ProductVariant updatedVariant) {
        ProductVariant existing = getProductVariantOrThrow(updatedVariant.getProductVariantId());

        if (!existing.getColor().equals(updatedVariant.getColor())
                || existing.getSize() != updatedVariant.getSize()
                || !existing.getProduct().equals(updatedVariant.getProduct())) {
            checkVariantUniqueness(updatedVariant);
        }

        existing.setColor(updatedVariant.getColor());
        existing.setSize(updatedVariant.getSize());
        existing.setQuantity(updatedVariant.getQuantity());
        existing.setSalePrice(updatedVariant.getSalePrice());
        existing.setSku(generateSku(updatedVariant));

        return productVariantRepository.save(existing);
    }

    public void deleteProductVariant(Long id) {
        productVariantRepository.delete(getProductVariantOrThrow(id));
    }
}
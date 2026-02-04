package com.example.tjfw.service;

import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.exceptions.AlreadyExistsException;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    public ProductVariantService(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    private void checkVariantUniqueness(ProductVariant productVariant) {
        boolean variantExists = productVariantRepository.existsByProductAndColorAndSize(productVariant.getProduct(), productVariant.getColor(), productVariant.getSize());
        if (variantExists) {
            throw new AlreadyExistsException("Product variant already exists");
        }

    }

    private String generateSku(ProductVariant variant) {
        String productCode = variant.getProduct().getProductName().substring(0, 3).toUpperCase();
        String colorCode = variant.getColor().substring(0, 3).toUpperCase();
        String sizeCode = String.valueOf(variant.getSize()).toUpperCase();

        return productCode + "-" + colorCode + "-" + sizeCode;
    }

    private ProductVariant getProductVariantOrThrow(Long id) {
        return productVariantRepository.findById(id).orElseThrow(() -> new NotFoundException("Product variant not found"));
    }

    public ProductVariant findProductVariantById(Long id) {
        return getProductVariantOrThrow(id);
    }

    public List<ProductVariant> findAllProductVariants() {
        return productVariantRepository.findAll();
    }

    public ProductVariant createNewProductVariant(ProductVariant productVariant) {
        //check if the product variant is unique
        checkVariantUniqueness(productVariant);
        //generate sku (sku is unique from entity so no need for check)
        productVariant.setSku(generateSku(productVariant));
        //save this new product variant to the repository
        return productVariantRepository.save(productVariant);
    }

    public void deleteProductVariant(Long id) {
        ProductVariant productVariant = getProductVariantOrThrow(id);
        productVariantRepository.delete(productVariant);
    }
}

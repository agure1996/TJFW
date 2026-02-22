package com.example.tjfw.service;

import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductImage;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.entity.Supplier;
import com.example.tjfw.exceptions.AlreadyExistsException;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductImageRepository;
import com.example.tjfw.repository.ProductRepository;
import com.example.tjfw.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierService supplierService;
    private final ProductImageRepository imageRepository;
    private final ProductVariantRepository variantRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(ProductRepository productRepository,
                          SupplierService supplierService,
                          ProductImageRepository imageRepository,
                          ProductVariantRepository variantRepository,
                          CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.supplierService = supplierService;
        this.imageRepository = imageRepository;
        this.variantRepository = variantRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Product findById(Long id) {
        return getProductOrThrow(id);
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product createNewProduct(Product product, Long supplierId) {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new AlreadyExistsException("Product already exists");
        }
        if (supplierId != null) {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            product.setSupplier(supplier);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct, Long supplierId) {
        Product existing = getProductOrThrow(id);

        existing.setProductName(updatedProduct.getProductName());
        existing.setProductType(updatedProduct.getProductType());
        existing.setDescription(updatedProduct.getProductDescription());

        if (supplierId != null) {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            existing.setSupplier(supplier);
        }

        return productRepository.save(existing);
    }

    /**
     * FIX: Deletes all associated product images from Cloudinary and the DB
     * before deleting the product, to avoid the FK constraint violation on
     * the product_images table (fkqnq71xsohugpqwf3c9gxmsuy).
     */
    public void deleteProduct(Long id) {
        Product product = getProductOrThrow(id);

        // Delete product-level images from Cloudinary and DB
        deleteImagesForEntity(imageRepository.findByProductIdOrderByDisplayOrderAsc(id));

        // Delete variant-level images for every variant belonging to this product
        List<ProductVariant> variants = variantRepository.findAllVariantsByProductId(id);
        for (ProductVariant variant : variants) {
            deleteImagesForEntity(
                    imageRepository.findByVariantIdOrderByDisplayOrderAsc(variant.getProductVariantId())
            );
        }

        productRepository.delete(product);
    }

    private void deleteImagesForEntity(List<ProductImage> images) {
        for (ProductImage image : images) {
            try {
                cloudinaryService.deleteImage(image.getPublicId());
            } catch (IOException e) {
                // Best-effort Cloudinary cleanup — don't block DB deletion
                System.err.println("Failed to delete image from Cloudinary: "
                        + image.getPublicId() + " — " + e.getMessage());
            }
        }
        imageRepository.deleteAll(images);
    }
}
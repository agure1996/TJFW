package com.example.tjfw.service;

import com.example.tjfw.dto.productimage.PhotoUploadResult;
import com.example.tjfw.dto.productimage.ProductImageDTO;
import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductImage;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductImageService {

    private final ProductImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductService productService;

    public ProductImageService(ProductImageRepository imageRepository,
                               CloudinaryService cloudinaryService,
                               ProductService productService) {
        this.imageRepository = imageRepository;
        this.cloudinaryService = cloudinaryService;
        this.productService = productService;
    }

    public ProductImageDTO uploadImage(Long productId, MultipartFile file) throws IOException {
        Product product = productService.findById(productId);

        // Upload to Cloudinary
        PhotoUploadResult result = cloudinaryService.uploadImage(file, "tjfw/products/" + productId);

        // Get current images
        List<ProductImage> currentImages = imageRepository
                .findByProduct_ProductIdOrderByDisplayOrderAsc(productId);

        // Create image entity
        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setImageUrl(result.url());
        image.setPublicId(result.publicId());
        image.setIsMain(currentImages.isEmpty()); // first image is main
        image.setDisplayOrder(currentImages.size());
        image.setAltText(product.getProductName() + " image");

        ProductImage saved = imageRepository.save(image);

        return mapToDTO(saved);
    }

    public void deleteImage(Long productId, Long imageId) throws IOException {
        Product product = productService.findById(productId);

        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));

        if (!image.getProduct().getProductId().equals(productId)) {
            throw new IllegalArgumentException("Image does not belong to this product");
        }

        if (image.getIsMain()) {
            List<ProductImage> otherImages = imageRepository
                    .findByProduct_ProductIdOrderByDisplayOrderAsc(productId)
                    .stream()
                    .filter(img -> !img.getImageId().equals(imageId))
                    .toList();

            if (!otherImages.isEmpty()) {
                throw new IllegalArgumentException(
                        "Cannot delete main image. Set another image as main first."
                );
            }
        }

        // Delete from Cloudinary
        cloudinaryService.deleteImage(image.getPublicId());

        // Delete from database
        imageRepository.delete(image);
    }

    public ProductImageDTO setMainImage(Long productId, Long imageId) {
        Product product = productService.findById(productId);

        ProductImage newMainImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));

        if (!newMainImage.getProduct().getProductId().equals(productId)) {
            throw new IllegalArgumentException("Image does not belong to this product");
        }

        // Unset all main flags for this product
        List<ProductImage> allImages = imageRepository
                .findByProduct_ProductIdOrderByDisplayOrderAsc(productId);

        allImages.forEach(img -> img.setIsMain(false));
        imageRepository.saveAll(allImages);

        // Set new main
        newMainImage.setIsMain(true);
        ProductImage saved = imageRepository.save(newMainImage);

        return mapToDTO(saved);
    }

    public List<ProductImageDTO> getProductImages(Long productId) {
        productService.findById(productId); // verify product exists

        return imageRepository.findByProduct_ProductIdOrderByDisplayOrderAsc(productId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProductImageDTO mapToDTO(ProductImage image) {
        return new ProductImageDTO(
                image.getImageId(),
                image.getImageUrl(),
                image.getPublicId(),
                image.getIsMain(),
                image.getDisplayOrder(),
                image.getAltText()
        );
    }
}
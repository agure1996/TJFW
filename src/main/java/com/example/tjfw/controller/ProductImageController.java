package com.example.tjfw.controller;

import com.example.tjfw.dto.productimage.ProductImageDTO;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.ProductImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductImageController {

    private final ProductImageService imageService;

    public ProductImageController(ProductImageService imageService) {
        this.imageService = imageService;
    }

    // ========================
    // PRODUCT IMAGE ENDPOINTS
    // ========================

    @GetMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<List<ProductImageDTO>>> getProductImages(
            @PathVariable Long productId) {
        List<ProductImageDTO> images = imageService.getProductImages(productId, null);
        return ResponseEntity.ok(new ApiResponse<>("Product images retrieved successfully", images));
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<ProductImageDTO>> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {
        try {
            ProductImageDTO image = imageService.uploadImage(productId, null, file);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Product image uploaded successfully", image));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to upload image: " + e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        try {
            imageService.deleteImage(imageId, productId, null);
            return ResponseEntity.ok(new ApiResponse<>("Image deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("/{productId}/images/{imageId}/set-main")
    public ResponseEntity<ApiResponse<ProductImageDTO>> setMainProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        try {
            ProductImageDTO image = imageService.setMainImage(imageId, productId, null);
            return ResponseEntity.ok(new ApiResponse<>("Main image updated successfully", image));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    // ========================
    // VARIANT IMAGE ENDPOINTS
    // ========================

    @GetMapping("/variants/{variantId}/images")
    public ResponseEntity<ApiResponse<List<ProductImageDTO>>> getVariantImages(
            @PathVariable Long variantId) {
        List<ProductImageDTO> images = imageService.getProductImages(null, variantId);
        return ResponseEntity.ok(new ApiResponse<>("Variant images retrieved successfully", images));
    }

    @PostMapping("/variants/{variantId}/images")
    public ResponseEntity<ApiResponse<ProductImageDTO>> uploadVariantImage(
            @PathVariable Long variantId,
            @RequestParam("file") MultipartFile file) {
        try {
            ProductImageDTO image = imageService.uploadImage(null, variantId, file);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Variant image uploaded successfully", image));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to upload image: " + e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/variants/{variantId}/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteVariantImage(
            @PathVariable Long variantId,
            @PathVariable Long imageId) {
        try {
            imageService.deleteImage(imageId, null, variantId);
            return ResponseEntity.ok(new ApiResponse<>("Image deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("/variants/{variantId}/images/{imageId}/set-main")
    public ResponseEntity<ApiResponse<ProductImageDTO>> setMainVariantImage(
            @PathVariable Long variantId,
            @PathVariable Long imageId) {
        try {
            ProductImageDTO image = imageService.setMainImage(imageId, null, variantId);
            return ResponseEntity.ok(new ApiResponse<>("Main image updated successfully", image));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
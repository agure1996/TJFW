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
@RequestMapping("/products/{productId}/images")
public class ProductImageController {

    private final ProductImageService imageService;

    public ProductImageController(ProductImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductImageDTO>>> getImages(
            @PathVariable Long productId
    ) {
        List<ProductImageDTO> images = imageService.getProductImages(productId);
        return ResponseEntity.ok(new ApiResponse<>("Images retrieved successfully", images));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductImageDTO>> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            ProductImageDTO image = imageService.uploadImage(productId, file);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Image uploaded successfully", image));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to upload image: " + e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
            @PathVariable Long productId,
            @PathVariable Long imageId
    ) {
        try {
            imageService.deleteImage(productId, imageId);
            return ResponseEntity.ok(new ApiResponse<>("Image deleted successfully", null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to delete image: " + e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("/{imageId}/set-main")
    public ResponseEntity<ApiResponse<ProductImageDTO>> setMainImage(
            @PathVariable Long productId,
            @PathVariable Long imageId
    ) {
        try {
            ProductImageDTO image = imageService.setMainImage(productId, imageId);
            return ResponseEntity.ok(new ApiResponse<>("Main image updated successfully", image));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
package com.example.tjfw.controller;

import com.example.tjfw.mapper.ProductMapper;
import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.dto.productvariant.RequestProductVariantDTO;
import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.ProductService;
import com.example.tjfw.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductVariantController {

    private final ProductVariantService productVariantService;
    private final ProductMapper productMapper;
    private final ProductService productService;

    public ProductVariantController(ProductVariantService productVariantService,
                                    ProductMapper productMapper,
                                    ProductService productService) {
        this.productVariantService = productVariantService;
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @GetMapping("/{id}/variants")
    public ResponseEntity<ApiResponse<List<ProductVariantDTO>>> getVariants(@PathVariable Long id) {
        List<ProductVariantDTO> variants = productVariantService
                .findAllByProductId(id)
                .stream()
                .map(productMapper::toVariantDTO)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("Variants found", variants));
    }

    @GetMapping("/variants")
    public ResponseEntity<ApiResponse<List<ProductVariantDTO>>> listAllVariants() {
        List<ProductVariantDTO> variants = productVariantService.findAllProductVariantsDTO();
        return ResponseEntity.ok(new ApiResponse<>("All variants", variants));
    }

    @GetMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> getVariant(@PathVariable Long variantId) {
        ProductVariant variant = productVariantService.findProductVariantById(variantId);
        return ResponseEntity.ok(new ApiResponse<>("Variant found", productMapper.toVariantDTO(variant)));
    }

    @PostMapping("/{productId}/variants")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> createVariant(
            @PathVariable Long productId,
            @Valid @RequestBody RequestProductVariantDTO requestDTO) {
        Product product = productService.findById(productId);
        ProductVariant variant = new ProductVariant(
                product,
                requestDTO.getColor(),
                requestDTO.getSize(),
                requestDTO.getQuantity(),
                requestDTO.getSalePrice());
        ProductVariant saved = productVariantService.createNewProductVariant(variant);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Variant created", productMapper.toVariantDTO(saved)));
    }

    @PutMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> updateVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody RequestProductVariantDTO requestDTO) {
        ProductVariant existing = productVariantService.findProductVariantById(variantId);
        existing.setColor(requestDTO.getColor());
        existing.setSize(requestDTO.getSize());
        existing.setQuantity(requestDTO.getQuantity());
        existing.setSalePrice(requestDTO.getSalePrice());
        ProductVariant updated = productVariantService.updateProductVariant(existing);
        return ResponseEntity.ok(new ApiResponse<>("Variant updated", productMapper.toVariantDTO(updated)));
    }

    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(@PathVariable Long variantId) {
        productVariantService.deleteProductVariant(variantId);
        return ResponseEntity.ok(new ApiResponse<>("Variant deleted", null));
    }
}
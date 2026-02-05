package com.example.tjfw.controller;

import com.example.tjfw.dto.product.ProductDTO;
import com.example.tjfw.dto.product.ProductRequestDTO;
import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.dto.productvariant.RequestProductVariantDTO;
import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductType;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.ProductService;
import com.example.tjfw.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductVariantService productVariantService;

    public ProductController(ProductService productService, ProductVariantService productVariantService) {
        this.productService = productService;
        this.productVariantService = productVariantService;
    }

    // ========================
    // PRODUCT CRUD
    // ========================
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> findAll() {
        List<ProductDTO> products = productService.findAllProducts().stream()
                .map(p -> new ProductDTO(
                        p.getProductId(),
                        p.getProductName(),
                        p.getProductType().toString(),
                        p.getProductDescription()))
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("List of Products found", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> findById(@PathVariable Long id) {
        Product p = productService.findById(id);
        ProductDTO dto = new ProductDTO(p.getProductId(), p.getProductName(), p.getProductType().toString(), p.getProductDescription());
        return ResponseEntity.ok(new ApiResponse<>("Product found", dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        // Convert string to enum
        ProductType type;
        try {
            type = ProductType.valueOf(request.getProductType().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid product type: " + request.getProductType(), null));
        }

        Product product = new Product(request.getProductName(), type, request.getProductDescription());
        Product created = productService.createNewProduct(product);
        ProductDTO dto = new ProductDTO(created.getProductId(), created.getProductName(), created.getProductType().toString(), created.getProductDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Product created successfully", dto));
    }


    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> createProductBulk(@Valid @RequestBody List<ProductRequestDTO> requests) {
        List<ProductDTO> createdList = new ArrayList<>();
        for (ProductRequestDTO request : requests) {
            ProductType type;
            try {
                type = ProductType.valueOf(request.getProductType().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Invalid product type: " + request.getProductType(), null));
            }

            Product p = new Product(request.getProductName(), type, request.getProductDescription());
            Product created = productService.createNewProduct(p);
            createdList.add(new ProductDTO(created.getProductId(), created.getProductName(), created.getProductType().toString(), created.getProductDescription()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Products bulk added successfully", createdList));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully", null));
    }

    // ========================
    // PRODUCT VARIANT CRUD
    // ========================
    @GetMapping("/{productId}/variants")
    public ResponseEntity<ApiResponse<List<ProductVariantDTO>>> getVariants(@PathVariable Long productId) {

        // ensure product exists
        productService.findById(productId);

        List<ProductVariantDTO> variants = productVariantService
                .findAllByProductId(productId)
                .stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>("Variants found", variants));
    }

    @GetMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> getVariant(@PathVariable Long variantId) {
        ProductVariant variant = productVariantService.findProductVariantById(variantId);
        return ResponseEntity.ok(new ApiResponse<>("Variant found", mapToDTO(variant)));
    }

    @PostMapping("/{productId}/variants")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> createVariant(
            @PathVariable Long productId,
            @Valid @RequestBody RequestProductVariantDTO requestDTO
    ) {
        Product product = productService.findById(productId);

        ProductVariant variant = new ProductVariant(
                product,
                requestDTO.getColor(),
                requestDTO.getSize(),
                requestDTO.getQuantity(),
                requestDTO.getSalePrice()
        );

        ProductVariant saved = productVariantService.createNewProductVariant(variant);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Variant created", mapToDTO(saved)));
    }

    @PutMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> updateVariant(
            @PathVariable Long variantId,
            @Valid @RequestBody RequestProductVariantDTO requestDTO
    ) {
        ProductVariant existing = productVariantService.findProductVariantById(variantId);

        existing.setColor(requestDTO.getColor());
        existing.setSize(requestDTO.getSize());
        existing.setQuantity(requestDTO.getQuantity());
        existing.setSalePrice(requestDTO.getSalePrice());

        // SKU regenerated if needed in service
        ProductVariant updated = productVariantService.createNewProductVariant(existing);
        return ResponseEntity.ok(new ApiResponse<>("Variant updated", mapToDTO(updated)));
    }

    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(@PathVariable Long variantId) {
        productVariantService.deleteProductVariant(variantId);
        return ResponseEntity.ok(new ApiResponse<>("Variant deleted", null));
    }

    // ========================
    // Helper method to map ProductVariant -> DTO
    // ========================
    private ProductVariantDTO mapToDTO(ProductVariant variant) {
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
}

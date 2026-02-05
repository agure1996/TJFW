package com.example.tjfw.controller;

import com.example.tjfw.dto.product.ProductDTO;
import com.example.tjfw.dto.product.ProductRequestDTO;
import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.entity.Product;
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

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductRequestDTO>>> findAll() {

        List<Product> products = productService.findAllProducts();
        List<ProductRequestDTO> pDTO = products.stream().map(p ->
                new ProductRequestDTO(p.getProductName(), p.getProductType().toString(), p.getProductDescription())).toList();
        return ResponseEntity
                .ok(new ApiResponse<>("List of Products found", pDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        ProductDTO foundProduct = new ProductDTO(product.getProductId(), product.getProductName(), product.getProductType().toString(), product.getProductDescription());
        return ResponseEntity
                .ok(new ApiResponse<>("Product found", foundProduct));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductRequestDTO>> createProduct(@Valid @RequestBody Product product) {
        Product createProduct = productService.createNewProduct(product);
        ProductRequestDTO newProduct = new ProductRequestDTO(createProduct.getProductName(), createProduct.getProductType().toString(), createProduct.getProductDescription());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product created successfully", newProduct));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<ProductRequestDTO>>> createProductBulk(@Valid @RequestBody List<Product> products) {
        List<ProductRequestDTO> productBulk = new ArrayList<>();
        for (Product product : products) {
            Product createProduct = productService.createNewProduct(product);

            productBulk.add(
                    new ProductRequestDTO(
                            createProduct.getProductName(),
                            createProduct.getProductType().toString(),
                            createProduct.getProductDescription()
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product bulk added successfully", productBulk));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .ok(new ApiResponse<>("Product deleted successfully", null));
    }

    // ========================
    // Endpoints for Product Variant
    // ========================
    @GetMapping("/{id}/variants")
    public ResponseEntity<ApiResponse<List<ProductVariantDTO>>> getVariants(@PathVariable Long id) {
        productService.findById(id); // ensure product exists

        List<ProductVariantDTO> variants = productVariantService
                .findAllProductVariants()
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

    @PostMapping("/{id}/variants")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> createVariant(
            @PathVariable Long id,
            @RequestBody ProductVariant variantRequest
    ) {
        // Ensure product exists
        productService.findById(id);
        variantRequest.setProduct(productService.findById(id));

        ProductVariant savedVariant = productVariantService.createNewProductVariant(variantRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Variant created", mapToDTO(savedVariant)));
    }

    @PutMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantDTO>> updateVariant(@PathVariable Long variantId, @RequestBody ProductVariant updatedVariant) {
        ProductVariant existing = productVariantService.findProductVariantById(variantId);
        // Update fields
        existing.setColor(updatedVariant.getColor());
        existing.setSize(updatedVariant.getSize());
        existing.setSalePrice(updatedVariant.getSalePrice());
        existing.setQuantity(updatedVariant.getQuantity());
        // SKU can be regenerated if needed
        existing.setSku(existing.getSku()); // or call generateSku logic

        ProductVariant saved = productVariantService.createNewProductVariant(existing);
        return ResponseEntity.ok(new ApiResponse<>("Variant updated", mapToDTO(saved)));
    }

    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(@PathVariable Long variantId) {
        productVariantService.deleteProductVariant(variantId);
        return ResponseEntity.ok(new ApiResponse<>("Variant deleted", null));
    }

    //helper method to map ProductVariant to
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

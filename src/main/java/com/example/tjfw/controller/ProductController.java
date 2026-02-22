package com.example.tjfw.controller;

import com.example.tjfw.dto.product.ProductDTO;
import com.example.tjfw.dto.product.RequestProductDTO;
import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductType;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> findAll() {
        List<ProductDTO> products = productService.findAllProducts().stream()
                .map(p -> new ProductDTO(
                        p.getProductId(),
                        p.getProductName(),
                        p.getProductType(),
                        p.getProductDescription(),
                        p.getSupplier() != null ? p.getSupplier().getSupplierId() : null))
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("List of Products found", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> findById(@PathVariable Long id) {
        Product p = productService.findById(id);
        ProductDTO dto = new ProductDTO(
                p.getProductId(),
                p.getProductName(),
                p.getProductType(),
                p.getProductDescription(),
                p.getSupplier() != null ? p.getSupplier().getSupplierId() : null);
        return ResponseEntity.ok(new ApiResponse<>("Product found", dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @Valid @RequestBody RequestProductDTO request) {
        ProductType type;
        try {
            type = request.getProductType();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid product type: " + request.getProductType(), null));
        }

        Product product = new Product(request.getProductName(), type, request.getProductDescription());
        Product created = productService.createNewProduct(product, request.getSupplierId());
        ProductDTO dto = new ProductDTO(
                created.getProductId(),
                created.getProductName(),
                created.getProductType(),
                created.getProductDescription(),
                created.getSupplier() != null ? created.getSupplier().getSupplierId() : null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product created successfully", dto));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> createProductBulk(
            @Valid @RequestBody List<RequestProductDTO> requests) {
        List<ProductDTO> createdList = new ArrayList<>();
        for (RequestProductDTO request : requests) {
            ProductType type;
            try {
                type = request.getProductType();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("Invalid product type: " + request.getProductType(), null));
            }
            Product p = new Product(request.getProductName(), type, request.getProductDescription());
            Product created = productService.createNewProduct(p, request.getSupplierId());
            createdList.add(new ProductDTO(
                    created.getProductId(),
                    created.getProductName(),
                    created.getProductType(),
                    created.getProductDescription(),
                    created.getSupplier() != null ? created.getSupplier().getSupplierId() : null));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Products bulk added successfully", createdList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody RequestProductDTO request) {
        ProductType type;
        try {
            type = request.getProductType();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Invalid product type: " + request.getProductType(), null));
        }
        Product product = new Product(request.getProductName(), type, request.getProductDescription());
        Product updated = productService.updateProduct(id, product, request.getSupplierId());
        ProductDTO dto = new ProductDTO(
                updated.getProductId(),
                updated.getProductName(),
                updated.getProductType(),
                updated.getProductDescription(),
                updated.getSupplier() != null ? updated.getSupplier().getSupplierId() : null);
        return ResponseEntity.ok(new ApiResponse<>("Product updated successfully", dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully", null));
    }

    @GetMapping("/product-types")
    public List<Map<String, String>> productTypes() {
        return Arrays.stream(ProductType.values())
                .map(t -> Map.of("key", t.name(), "label", t.getDisplayName()))
                .toList();
    }
}
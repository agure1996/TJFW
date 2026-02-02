package com.example.tjfw.controller;

import com.example.tjfw.dto.ProductDTO;
import com.example.tjfw.dto.ProductRequestDTO;
import com.example.tjfw.entity.Product;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public String sayHi() {
        return "Hello World!";
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .ok(new ApiResponse<>("Product deleted successfully", null));
    }

}

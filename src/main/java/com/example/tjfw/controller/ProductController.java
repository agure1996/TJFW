package com.example.tjfw.controller;

import com.example.tjfw.entity.Product;
import com.example.tjfw.exceptions.NotFoundException;
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
    public ResponseEntity<ApiResponse<Product>> findById(@PathVariable Long id) {

        Product product = productService.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));

        return ResponseEntity.ok(
                new ApiResponse<>("Successfully found product with id " + id, product));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product product) {
            Product newProduct = productService.createNewProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Product created successfully", newProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully", null));
    }

}

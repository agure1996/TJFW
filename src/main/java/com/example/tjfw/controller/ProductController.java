package com.example.tjfw.controller;

import com.example.tjfw.dto.product.ProductDTO;
import com.example.tjfw.dto.product.ProductRequestDTO;
import com.example.tjfw.entity.Product;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.ProductService;
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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/hi")
    public String sayHi() {
        return "Hello World!";
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductRequestDTO>>> findAll() {

        List<Product> products = productService.findAllProducts();
        List<ProductRequestDTO> pDTO = products.stream().map(p ->
                new ProductRequestDTO( p.getProductName(),p.getProductType().toString(),p.getProductDescription())).toList();
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

}

package com.example.tjfw.service;

import com.example.tjfw.entity.Product;
import com.example.tjfw.exceptions.AlreadyExistsException;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(Long id) {
        return getProductOrThrow(id);
    }

    //Created to stop code duplication
    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product createNewProduct(Product product) {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new AlreadyExistsException("Product already exists");
        }
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductOrThrow(id);
        productRepository.delete(product);
    }
}

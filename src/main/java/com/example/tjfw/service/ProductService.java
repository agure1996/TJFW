package com.example.tjfw.service;

import com.example.tjfw.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {this.productRepository = productRepository;}

}

package com.example.tjfw.service;
import com.example.tjfw.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    public ProductVariantService(ProductVariantRepository productVariantRepository) {this.productVariantRepository = productVariantRepository;}


}

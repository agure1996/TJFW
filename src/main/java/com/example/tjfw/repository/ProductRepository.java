package com.example.tjfw.repository;

import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<ProductVariant> findByProductId(Long productId);
    List<Purchase> findBySupplierId(Long supplierId);
    Optional<Product> findProductById(Long id);
}

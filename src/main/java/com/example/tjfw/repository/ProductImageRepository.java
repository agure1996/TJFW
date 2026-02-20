package com.example.tjfw.repository;

import com.example.tjfw.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProduct_ProductIdOrderByDisplayOrderAsc(Long productId);

    Optional<ProductImage> findByProduct_ProductIdAndIsMainTrue(Long productId);

    void deleteByProduct_ProductId(Long productId);
}
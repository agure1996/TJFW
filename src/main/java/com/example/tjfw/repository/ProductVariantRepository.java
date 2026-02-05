package com.example.tjfw.repository;

import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    boolean existsByProductAndColorAndSize(Product product, String color, int size);
    List<ProductVariant> findAllVariantsByProductId(Long id);
}

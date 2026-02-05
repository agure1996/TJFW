package com.example.tjfw.repository;

import com.example.tjfw.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    boolean existsBySupplierName(String supplierName);
    Optional<Supplier> findBySupplierId(Long supplierId);
}

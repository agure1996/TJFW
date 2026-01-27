package com.example.tjfw.service;
import com.example.tjfw.repository.SupplierRepository;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {this.supplierRepository = supplierRepository;}
}

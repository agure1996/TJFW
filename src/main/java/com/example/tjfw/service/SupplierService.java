package com.example.tjfw.service;
import com.example.tjfw.entity.Supplier;
import com.example.tjfw.exceptions.AlreadyExistsException;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
    // re-using a find/fail method
    private Supplier getSupplierOrThrow(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));
    }

    public Supplier createSupplier(Supplier supplier) {
        // Optional soft check
        if (supplierRepository.existsBySupplierName(supplier.getSupplierName())) {
            throw new AlreadyExistsException("Supplier with this name already exists");
        }
        return supplierRepository.save(supplier);
    }

    public Supplier getSupplierById(Long id) {
        return getSupplierOrThrow(id);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        Supplier existing = getSupplierOrThrow(id);

        existing.setSupplierName(updatedSupplier.getSupplierName());
        existing.setSupplierContactInfo(updatedSupplier.getSupplierContactInfo());
        existing.setNotes(updatedSupplier.getNotes());

        return supplierRepository.save(existing);
    }

    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierOrThrow(id);
        supplierRepository.delete(supplier);
    }
}

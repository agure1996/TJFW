package com.example.tjfw.controller;

import com.example.tjfw.dto.supplier.SupplierDTO;
import com.example.tjfw.dto.supplier.SupplierRequestDTO;
import com.example.tjfw.entity.Supplier;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SupplierDTO>>> getAllSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers()
                .stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>("Suppliers found", suppliers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDTO>> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(new ApiResponse<>("Supplier found", mapToDTO(supplier)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SupplierDTO>> createSupplier(@Valid @RequestBody SupplierRequestDTO request) {
        Supplier supplier = new Supplier();
        supplier.setSupplierName(request.supplierName());
        supplier.setSupplierContactInfo(request.supplierContactInfo());
        supplier.setNotes(request.notes());

        Supplier saved = supplierService.createSupplier(supplier);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Supplier created", mapToDTO(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDTO>> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO request
    ) {
        Supplier existing = supplierService.getSupplierById(id);

        existing.setSupplierName(request.supplierName());
        existing.setSupplierContactInfo(request.supplierContactInfo());
        existing.setNotes(request.notes());

        Supplier updated = supplierService.updateSupplier(existing.getSupplierId(), existing);

        return ResponseEntity.ok(new ApiResponse<>("Supplier updated", mapToDTO(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(new ApiResponse<>("Supplier deleted", null));
    }

    // ========================
    // DTO Mapper
    // ========================
    private SupplierDTO mapToDTO(Supplier supplier) {
        return new SupplierDTO(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getSupplierContactInfo(),
                supplier.getNotes()
        );
    }
}

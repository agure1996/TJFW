package com.example.tjfw.mapper;

import com.example.tjfw.dto.supplier.SupplierDTO;
import com.example.tjfw.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierDTO toDTO(Supplier supplier) {
        return new SupplierDTO(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getSupplierContactInfo(),
                supplier.getNotes()
        );
    }
}
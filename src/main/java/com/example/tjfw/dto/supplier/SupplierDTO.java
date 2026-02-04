package com.example.tjfw.dto.supplier;

import jakarta.persistence.Column;

public record SupplierDTO(Long supplierId, String supplierName, String supplierContactInfo, String notes) {}

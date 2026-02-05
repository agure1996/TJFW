package com.example.tjfw.dto.supplier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestSupplierDTO(
        @NotBlank(message = "Supplier name is required") @Size(max = 255, message = "Supplier name must be at most 255 characters") String supplierName,
        @NotBlank(message = "Contact info is required") String supplierContactInfo, String notes) {
}


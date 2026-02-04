package com.example.tjfw.dto.purchase;
import com.example.tjfw.dto.supplier.SupplierDTO;
import com.example.tjfw.entity.PurchaseType;
import java.time.LocalDateTime;

public record PurchaseDTO(Long purchaseId, SupplierDTO supplier, PurchaseType purchaseType, LocalDateTime purchaseDate) {}

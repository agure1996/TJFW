package com.example.tjfw.dto.purchase;
import com.example.tjfw.entity.PurchaseType;
import com.example.tjfw.entity.Supplier;
import java.time.LocalDateTime;

public record PurchaseDTO(Long purchaseId, Supplier supplier, PurchaseType purchaseType, LocalDateTime purchaseDate) {}

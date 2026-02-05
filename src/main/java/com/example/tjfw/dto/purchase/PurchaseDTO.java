package com.example.tjfw.dto.purchase;

import com.example.tjfw.dto.purchaseitem.PurchaseItemDTO;
import com.example.tjfw.dto.supplier.SupplierDTO;
import com.example.tjfw.entity.PurchaseType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PurchaseDTO(Long purchaseId, SupplierDTO supplier, PurchaseType purchaseType, LocalDateTime purchaseDate,
                          BigDecimal totalAmount, List<PurchaseItemDTO> items) {
}

package com.example.tjfw.dto.purchase;

import com.example.tjfw.entity.PurchaseType;
import java.time.LocalDateTime;

public class PurchaseRequestDTO {
    private Long supplierId;
    private PurchaseType purchaseType;
    private LocalDateTime purchaseDate;

    public PurchaseRequestDTO() {}

    public PurchaseRequestDTO(Long supplierId, PurchaseType purchaseType, LocalDateTime purchaseDate){
        this.supplierId = supplierId;
        this.purchaseType = purchaseType;
        this.purchaseDate = purchaseDate;
    }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public PurchaseType getPurchaseType() { return purchaseType; }
    public void setPurchaseType(PurchaseType purchaseType) { this.purchaseType = purchaseType; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }
}


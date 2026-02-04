package com.example.tjfw.dto.purchase;

import com.example.tjfw.entity.PurchaseType;
import com.example.tjfw.entity.Supplier;

import java.time.LocalDateTime;

public class PurchaseRequestDTO {

    private Supplier supplier;
    private PurchaseType purchaseType;
    private LocalDateTime purchaseDate;

    public PurchaseRequestDTO(Supplier supplier, PurchaseType purchaseType, LocalDateTime purchaseDate){
        this.supplier = supplier;
        this.purchaseType = purchaseType;
        this.purchaseDate = purchaseDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(PurchaseType purchaseType) {
        this.purchaseType = purchaseType;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}

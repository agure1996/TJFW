package com.example.tjfw.dto.purchase;

import com.example.tjfw.dto.purchaseitem.RequestPurchaseItemDTO;
import com.example.tjfw.entity.PurchaseType;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseRequestDTO {
    private Long supplierId;
    private PurchaseType purchaseType;
    private LocalDateTime purchaseDate;
    private List<RequestPurchaseItemDTO> items;

    public PurchaseRequestDTO() {}

    public PurchaseRequestDTO(Long supplierId, PurchaseType purchaseType, LocalDateTime purchaseDate, List<RequestPurchaseItemDTO> items) {
        this.supplierId = supplierId;
        this.purchaseType = purchaseType;
        this.purchaseDate = purchaseDate;
        this.items = items;
    }

    public Long getSupplierId() {return supplierId;}

    public void setSupplierId(Long supplierId) {this.supplierId = supplierId;}

    public PurchaseType getPurchaseType() {return purchaseType;}

    public void setPurchaseType(PurchaseType purchaseType) {this.purchaseType = purchaseType;}

    public LocalDateTime getPurchaseDate() {return purchaseDate;}

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<RequestPurchaseItemDTO> getItems() {return items;}

    public void setItems(List<RequestPurchaseItemDTO> items) {this.items = items;}
}

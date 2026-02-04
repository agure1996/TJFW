package com.example.tjfw.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseType purchaseType;
    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    protected Purchase() {}
    public Purchase(Supplier supplier, PurchaseType purchaseType, LocalDateTime purchaseDate) {
        this.supplier = supplier;
        this.purchaseType = purchaseType;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() {
        return id;
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

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", purchaseType=" + purchaseType +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}

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
    private PurchaseType purchase_type;
    @Column(nullable = false)
    private LocalDateTime purchase_date;

    protected Purchase() {
    }
    public Purchase(Supplier supplier, PurchaseType purchase_type, LocalDateTime purchase_date) {
        this.supplier = supplier;
        this.purchase_type = purchase_type;
        this.purchase_date = purchase_date;
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

    public PurchaseType getPurchase_type() {
        return purchase_type;
    }

    public void setPurchase_type(PurchaseType purchase_type) {
        this.purchase_type = purchase_type;
    }

    public LocalDateTime getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(LocalDateTime purchase_date) {
        this.purchase_date = purchase_date;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", purchaseType=" + purchase_type +
                ", purchaseDate=" + purchase_date +
                '}';
    }
}

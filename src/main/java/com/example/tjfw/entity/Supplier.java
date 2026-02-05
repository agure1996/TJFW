package com.example.tjfw.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;
    @Column(nullable = false)
    private String supplierName;
    @Column(nullable = false)
    private String supplierContactInfo;
    private String notes;

    public Supplier() {}

    public Supplier(String supplierName, String supplierContactInfo, String notes) {
        this.supplierName = supplierName;
        this.supplierContactInfo = supplierContactInfo;
        this.notes = notes;
    }

    public String getSupplierContactInfo() {
        return supplierContactInfo;
    }

    public void setSupplierContactInfo(String supplierContactInfo) {
        this.supplierContactInfo = supplierContactInfo;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Supplier{" + "id=" + supplierId + ", name='" + supplierName + '\'' + '}';
    }

}

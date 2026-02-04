package com.example.tjfw.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;
    private String supplierName;
    @Column(nullable = false)
    private String supplierContactInfo;
    private String notes;

    protected Supplier() {
    }

    public Supplier(String name, String contactInfo, String notes) {
        this.supplierName = name;
        this.supplierContactInfo = contactInfo;
        this.notes = notes;
    }

    public String getSupplierContactInfo() {
        return supplierContactInfo;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplier_id) {
        this.supplierId = supplier_id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplier_name) {
        this.supplierName = supplier_name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + supplierId +
                ", name='" + supplierName + '\'' +
                '}';
    }

}

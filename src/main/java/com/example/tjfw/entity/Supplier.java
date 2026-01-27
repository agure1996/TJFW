package com.example.tjfw.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplier_id;
    private String supplier_name;
    @Column(nullable = false)
    private String supplier_contactInfo;
    private String notes;

    protected Supplier() {
    }

    public Supplier(String name, String contactInfo, String notes) {
        this.supplier_name = name;
        this.supplier_contactInfo = contactInfo;
        this.notes = notes;
    }

    public String getSupplier_contactInfo() {
        return supplier_contactInfo;
    }

    public Long getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Long supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
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
                "id=" + supplier_id +
                ", name='" + supplier_name + '\'' +
                '}';
    }

}

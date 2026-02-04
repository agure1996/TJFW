package com.example.tjfw.dto.supplier;

public class SupplierRequestDTO {

    private String supplierName;
    private String supplierContactInfo;
    private String notes;

    public SupplierRequestDTO(String supplierName, String supplierContactInfo, String notes) {
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
}

package com.example.tjfw.dto.sale;

import com.example.tjfw.dto.saleitem.RequestSaleItemDTO;

import java.time.LocalDateTime;
import java.util.List;

public class RequestSaleDTO {

    private LocalDateTime saleDate;
    private List<RequestSaleItemDTO> items;
    private String customerName;
    private String customerContact;

    public RequestSaleDTO() {}

    public RequestSaleDTO(LocalDateTime saleDate, List<RequestSaleItemDTO> items,
                          String customerName, String customerContact) {
        this.saleDate = saleDate;
        this.items = items;
        this.customerName = customerName;
        this.customerContact = customerContact;
    }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

    public List<RequestSaleItemDTO> getItems() { return items; }
    public void setItems(List<RequestSaleItemDTO> items) { this.items = items; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerContact() { return customerContact; }
    public void setCustomerContact(String customerContact) { this.customerContact = customerContact; }
}

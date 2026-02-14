package com.example.tjfw.dto.sale;

import com.example.tjfw.dto.saleitem.SaleItemDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleDTO {
    private Long saleId;
    private LocalDateTime saleDate;
    private List<SaleItemDTO> items;
    private BigDecimal totalAmount;
    private String customerName;
    private String customerContact;

    public SaleDTO(Long saleId, LocalDateTime saleDate, List<SaleItemDTO> items, BigDecimal totalAmount,
                   String customerName, String customerContact) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.items = items;
        this.totalAmount = totalAmount;
        this.customerName = customerName;
        this.customerContact = customerContact;
    }

    // getters
    public Long getSaleId() { return saleId; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public List<SaleItemDTO> getItems() { return items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCustomerName() { return customerName; }
    public String getCustomerContact() { return customerContact; }
}

package com.example.tjfw.dto.sale;

import com.example.tjfw.dto.saleitem.RequestSaleItemDTO;
import java.time.LocalDateTime;
import java.util.List;

public class RequestSaleDTO {

    private LocalDateTime saleDate;
    private List<RequestSaleItemDTO> items;

    public RequestSaleDTO() {}

    public RequestSaleDTO(LocalDateTime saleDate, List<RequestSaleItemDTO> items) {
        this.saleDate = saleDate;
        this.items = items;
    }

    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
    public List<RequestSaleItemDTO> getItems() { return items; }
    public void setItems(List<RequestSaleItemDTO> items) { this.items = items; }
}


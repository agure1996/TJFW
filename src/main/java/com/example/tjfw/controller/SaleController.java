package com.example.tjfw.controller;

import com.example.tjfw.dto.sale.RequestSaleDTO;
import com.example.tjfw.dto.sale.SaleDTO;
import com.example.tjfw.entity.Sale;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaleDTO>> createSale(@RequestBody RequestSaleDTO request) {
        SaleDTO sale = saleService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Sale created", sale));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SaleDTO>>> getAllSales() {
        List<SaleDTO> sales = saleService.getAllSales().stream()
                .map(saleService::createSaleDTOFromEntity)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("Sales found", sales));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleDTO>> getSaleById(@PathVariable Long id) {
        Sale sale = saleService.getSaleById(id);
        SaleDTO saleDTO = saleService.createSaleDTOFromEntity(sale); // map entity → DTO
        return ResponseEntity.ok(new ApiResponse<>("Sale found", saleDTO));
    }
}

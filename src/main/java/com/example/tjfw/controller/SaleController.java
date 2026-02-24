package com.example.tjfw.controller;

import com.example.tjfw.dto.sale.RequestSaleDTO;
import com.example.tjfw.dto.sale.SaleDTO;
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<SaleDTO>>> getAllSales() {
        return ResponseEntity.ok(
                new ApiResponse<>("Sales found", saleService.getAllSales())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleDTO>> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>("Sale found", saleService.getSaleById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaleDTO>> createSale(@RequestBody RequestSaleDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Sale created", saleService.createSale(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleDTO>> updateSale(
            @PathVariable Long id,
            @RequestBody RequestSaleDTO request) {

        return ResponseEntity.ok(
                new ApiResponse<>("Sale updated", saleService.updateSale(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.ok(new ApiResponse<>("Sale deleted", null));
    }
}
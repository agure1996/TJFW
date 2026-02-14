package com.example.tjfw.controller;

import com.example.tjfw.dto.sale.RequestSaleDTO;
import com.example.tjfw.dto.sale.SaleDTO;
import com.example.tjfw.entity.Sale;
import com.example.tjfw.exceptions.NotFoundException;
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
        List<SaleDTO> sales = saleService.getAllSales().stream()
                .map(saleService::createSaleDTOFromEntity)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("Sales found", sales));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleDTO>> getSaleById(@PathVariable Long id) {
        try {
            Sale sale = saleService.getSaleById(id);
            SaleDTO saleDTO = saleService.createSaleDTOFromEntity(sale);
            return ResponseEntity.ok(new ApiResponse<>("Sale found", saleDTO));
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An unexpected error occurred", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SaleDTO>> createSale(@RequestBody RequestSaleDTO request) {
        try {
            SaleDTO sale = saleService.createSale(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Sale created", sale));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An unexpected error occurred", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleDTO>> updateSale(@PathVariable Long id, @RequestBody RequestSaleDTO request) {
        try {
            SaleDTO updatedSale = saleService.updateSale(id, request);
            return ResponseEntity.ok(new ApiResponse<>("Sale updated", updatedSale));
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An unexpected error occurred", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSale(@PathVariable Long id) {
        try {
            saleService.deleteSale(id);
            return ResponseEntity.ok(new ApiResponse<>("Sale deleted", null));
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An unexpected error occurred", null));
        }
    }
}

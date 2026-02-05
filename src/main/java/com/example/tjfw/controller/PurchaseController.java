package com.example.tjfw.controller;

import com.example.tjfw.dto.purchase.PurchaseDTO;
import com.example.tjfw.dto.purchase.RequestPurchaseDTO;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    public PurchaseController(PurchaseService purchaseService) {this.purchaseService = purchaseService;}

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseDTO>> createPurchase(@Valid @RequestBody RequestPurchaseDTO request) {
        PurchaseDTO created = purchaseService.createPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Purchase created", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseDTO>> getPurchase(@PathVariable Long id) {
        PurchaseDTO purchase = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(new ApiResponse<>("Purchase found", purchase));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PurchaseDTO>>> getAllPurchases() {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases().stream().toList();
        return ResponseEntity.ok(new ApiResponse<>("Purchases found", purchases));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseDTO>> updatePurchase(@PathVariable Long id, @Valid @RequestBody RequestPurchaseDTO request) {
        // Call service to update the purchase
        PurchaseDTO updatedPurchase = purchaseService.updatePurchase(id, request);
        return ResponseEntity.ok(new ApiResponse<>("Purchase updated successfully", updatedPurchase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok(new ApiResponse<>("Purchase deleted", null));
    }
}

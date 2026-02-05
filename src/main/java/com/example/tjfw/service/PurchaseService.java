package com.example.tjfw.service;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.dto.purchase.PurchaseDTO;
import com.example.tjfw.dto.purchase.PurchaseRequestDTO;
import com.example.tjfw.dto.purchaseitem.PurchaseItemDTO;
import com.example.tjfw.dto.purchaseitem.RequestPurchaseItemDTO;
import com.example.tjfw.dto.supplier.SupplierDTO;
import com.example.tjfw.entity.Purchase;
import com.example.tjfw.entity.PurchaseItem;
import com.example.tjfw.entity.Supplier;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.PurchaseRepository;
import com.example.tjfw.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseItemService purchaseItemService;

    public PurchaseService(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository, PurchaseItemService purchaseItemService) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseItemService = purchaseItemService;
    }

    private Purchase getPurchaseOrThrow(Long id) {
        return purchaseRepository.findById(id).orElseThrow(() -> new NotFoundException("Purchase with id " + id + " not found"));
    }

    private Supplier getSupplierOrThrow(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("Supplier with id " + id + " not found"));
    }

    public PurchaseDTO createPurchase(PurchaseRequestDTO request) {
        Supplier supplier = getSupplierOrThrow(request.getSupplierId());

        // Create purchase entity
        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setPurchaseType(request.getPurchaseType());

        purchaseRepository.save(purchase);

        // Add items if provided
        List<PurchaseItemDTO> itemDTOs = new ArrayList<>();
        if (request.getItems() != null) {
            for (RequestPurchaseItemDTO itemReq : request.getItems()) {
                PurchaseItem item = purchaseItemService.addPurchaseItem(purchase.getId(), itemReq.getProductVariantId(), itemReq.getQuantity(), itemReq.getCostPrice());
                itemDTOs.add(mapPurchaseItemToDTO(item));
            }
        }

        BigDecimal totalAmount = calculateTotalAmount(purchase);

        return new PurchaseDTO(purchase.getId(), mapSupplierToDTO(supplier), purchase.getPurchaseType(), purchase.getPurchaseDate(), totalAmount, itemDTOs);
    }

    private SupplierDTO mapSupplierToDTO(Supplier supplier) {
        return new SupplierDTO(supplier.getSupplierId(), supplier.getSupplierName(), supplier.getSupplierContactInfo(), supplier.getNotes());
    }

    private PurchaseItemDTO mapPurchaseItemToDTO(PurchaseItem item) {
        ProductVariantDTO variantDTO = new ProductVariantDTO(item.getProductVariant().getProductVariantId(),        // 1
                item.getProductVariant().getProduct().getProductId(),  // 2
                item.getProductVariant().getProduct().getProductName(),// 3
                item.getProductVariant().getColor(),                   // 4
                item.getProductVariant().getSalePrice(),               // 5
                item.getProductVariant().getSize(),                    // 6
                item.getProductVariant().getQuantity(),                // 7
                item.getProductVariant().getSku()                      // 8
        );

        return new PurchaseItemDTO(item.getId(),        // purchaseItemId
                variantDTO,          // nested ProductVariantDTO
                item.getQuantity(),  // quantity
                item.getCostPrice()  // costPrice
        );
    }


    public PurchaseDTO getPurchaseById(Long id) {
        Purchase purchase = getPurchaseOrThrow(id);

        List<PurchaseItemDTO> itemDTOs = purchase.getItems().stream().map(this::mapPurchaseItemToDTO).toList();

        BigDecimal totalAmount = calculateTotalAmount(purchase);

        return new PurchaseDTO(purchase.getId(), mapSupplierToDTO(purchase.getSupplier()), purchase.getPurchaseType(), purchase.getPurchaseDate(), totalAmount, itemDTOs);
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase updatePurchase(Long id, Purchase updatePurchase) {
        Purchase existingPurchase = getPurchaseOrThrow(id);
        existingPurchase.setPurchaseDate(updatePurchase.getPurchaseDate());
        if (updatePurchase.getSupplier() != null) {
            existingPurchase.setSupplier(updatePurchase.getSupplier());
        }
        return purchaseRepository.save(existingPurchase);
    }

    public void deletePurchase(Long id) {
        Purchase purchase = getPurchaseOrThrow(id);
        purchaseRepository.delete(purchase);
    }

    // Calculate total amount of a purchase dynamically
    public BigDecimal calculateTotalAmount(Purchase purchase) {
        return purchase.getItems().stream().map(item -> item.getCostPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

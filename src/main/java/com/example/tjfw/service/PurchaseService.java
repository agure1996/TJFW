package com.example.tjfw.service;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.dto.purchase.PurchaseDTO;
import com.example.tjfw.dto.purchase.RequestPurchaseDTO;
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

    // -------------------------
    // Internal helpers
    // -------------------------
    private Purchase getPurchaseOrThrow(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase with id " + id + " not found"));
    }

    private Supplier getSupplierOrThrow(Long id) {
        return supplierRepository.findBySupplierId(id)
                .orElseThrow(() -> new NotFoundException("Supplier with id " + id + " not found"));
    }

    private SupplierDTO mapSupplierToDTO(Supplier supplier) {
        return new SupplierDTO(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getSupplierContactInfo(),
                supplier.getNotes()
        );
    }

    private PurchaseItemDTO mapPurchaseItemToDTO(PurchaseItem item) {
        ProductVariantDTO variantDTO = new ProductVariantDTO(
                item.getProductVariant().getProductVariantId(),
                item.getProductVariant().getProduct().getProductId(),
                item.getProductVariant().getProduct().getProductName(),
                item.getProductVariant().getColor(),
                item.getProductVariant().getSalePrice(),
                item.getProductVariant().getSize(),
                item.getProductVariant().getQuantity(),
                item.getProductVariant().getSku()
        );

        return new PurchaseItemDTO(
                item.getId(),
                variantDTO,
                item.getQuantity(),
                item.getCostPrice()
        );
    }
    // -------------------------
    // Methods to support CRUD functions
    // -------------------------
    public PurchaseDTO createPurchase(RequestPurchaseDTO request) {
        Supplier supplier = getSupplierOrThrow(request.getSupplierId());

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setPurchaseType(request.getPurchaseType());

        purchaseRepository.save(purchase);

        // Add items if provided
        List<PurchaseItemDTO> itemDTOs = new ArrayList<>();
        if (request.getItems() != null) {
            for (RequestPurchaseItemDTO itemReq : request.getItems()) {
                PurchaseItem item = purchaseItemService.addPurchaseItem(
                        purchase.getId(),
                        itemReq.getProductVariantId(),
                        itemReq.getQuantity(),
                        itemReq.getCostPrice()
                );
                itemDTOs.add(mapPurchaseItemToDTO(item));
            }
        }

        BigDecimal totalAmount = calculateTotalAmount(purchase);

        return new PurchaseDTO(
                purchase.getId(),
                mapSupplierToDTO(supplier),
                purchase.getPurchaseType(),
                purchase.getPurchaseDate(),
                totalAmount,
                itemDTOs
        );
    }

    public PurchaseDTO getPurchaseById(Long id) {
        Purchase purchase = getPurchaseOrThrow(id);

        List<PurchaseItemDTO> itemDTOs = new ArrayList<>();
        if (purchase.getItems() != null) {
            for (PurchaseItem item : purchase.getItems()) {
                itemDTOs.add(mapPurchaseItemToDTO(item));
            }
        }

        BigDecimal totalAmount = calculateTotalAmount(purchase);

        return new PurchaseDTO(
                purchase.getId(),
                mapSupplierToDTO(purchase.getSupplier()),
                purchase.getPurchaseType(),
                purchase.getPurchaseDate(),
                totalAmount,
                itemDTOs
        );
    }

    public List<PurchaseDTO> getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAll();
        List<PurchaseDTO> result = new ArrayList<>();
        for (Purchase purchase : purchases) {
            result.add(getPurchaseById(purchase.getId()));
        }
        return result;
    }

    public PurchaseDTO updatePurchase(Long id, RequestPurchaseDTO request) {
        Purchase purchase = getPurchaseOrThrow(id);

        // Update supplier
        if (request.getSupplierId() != null) {
            purchase.setSupplier(getSupplierOrThrow(request.getSupplierId()));
        }

        // Update purchase date and type
        if (request.getPurchaseDate() != null) {
            purchase.setPurchaseDate(request.getPurchaseDate());
        }
        if (request.getPurchaseType() != null) {
            purchase.setPurchaseType(request.getPurchaseType());
        }

        purchaseRepository.save(purchase);

        // TODO: update items if request contains them

        return getPurchaseById(id);
    }

    public void deletePurchase(Long id) {
        Purchase purchase = getPurchaseOrThrow(id);
        purchaseRepository.delete(purchase);
    }

    // -------------------------
    // Calculate total amount
    // -------------------------
    public BigDecimal calculateTotalAmount(Purchase purchase) {
        if (purchase.getItems() == null) return BigDecimal.ZERO;

        return purchase.getItems().stream()
                .map(item -> item.getCostPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

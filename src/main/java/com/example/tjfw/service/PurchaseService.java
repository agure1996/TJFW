package com.example.tjfw.service;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.dto.purchase.PurchaseDTO;
import com.example.tjfw.dto.purchase.RequestPurchaseDTO;
import com.example.tjfw.dto.purchaseitem.PurchaseItemDTO;
import com.example.tjfw.dto.supplier.SupplierDTO;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.entity.Purchase;
import com.example.tjfw.entity.PurchaseItem;
import com.example.tjfw.entity.Supplier;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductVariantRepository;
import com.example.tjfw.repository.PurchaseItemRepository;
import com.example.tjfw.repository.PurchaseRepository;
import com.example.tjfw.repository.SupplierRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductVariantRepository productVariantRepository;
    private final PurchaseItemService purchaseItemService;
    private final PurchaseItemRepository purchaseItemRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository,
            PurchaseItemService purchaseItemService, ProductVariantRepository productVariantRepository
        ,PurchaseItemRepository purchaseItemRepository) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseItemService = purchaseItemService;
        this.productVariantRepository = productVariantRepository;
        this.purchaseItemRepository = purchaseItemRepository;
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
                supplier.getNotes());
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
                item.getProductVariant().getSku());

        return new PurchaseItemDTO(
                item.getId(),
                variantDTO,
                item.getQuantity(),
                item.getCostPrice());
    }

    // -------------------------
    // Methods to support CRUD functions
    // -------------------------

    @Transactional
    public PurchaseDTO createPurchase(RequestPurchaseDTO request) {
        Supplier supplier = getSupplierOrThrow(request.getSupplierId());

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setPurchaseType(request.getPurchaseType());

        purchase = purchaseRepository.save(purchase);

        // Add items from your RequestPurchaseItemDTO
        if (request.getItems() != null) {
            for (var itemReq : request.getItems()) {
                purchaseItemService.addPurchaseItem(
                        purchase.getId(),
                        itemReq.getProductVariantId(),
                        itemReq.getQuantity(),
                        itemReq.getCostPrice());
            }
        }

        return getPurchaseById(purchase.getId());
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
                itemDTOs);
    }

    public List<PurchaseDTO> getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAll();
        List<PurchaseDTO> result = new ArrayList<>();
        for (Purchase purchase : purchases) {
            result.add(getPurchaseById(purchase.getId()));
        }
        return result;
    }

    @Transactional
public PurchaseDTO updatePurchase(Long id, RequestPurchaseDTO request) {
    Purchase purchase = getPurchaseOrThrow(id);

    // -------------------------
    // 1️⃣ Update basic fields
    // -------------------------
    if (request.getSupplierId() != null) {
        purchase.setSupplier(getSupplierOrThrow(request.getSupplierId()));
    }
    if (request.getPurchaseDate() != null) {
        purchase.setPurchaseDate(request.getPurchaseDate());
    }
    if (request.getPurchaseType() != null) {
        purchase.setPurchaseType(request.getPurchaseType());
    }

    // -------------------------
    // 2️⃣ Fetch current items
    // -------------------------
    List<PurchaseItem> currentItems = purchaseItemService.getAllItemsForPurchase(purchase.getId());

    // -------------------------
    // 3️⃣ Remove items no longer in request
    // -------------------------
    for (PurchaseItem existingItem : new ArrayList<>(currentItems)) {
        boolean stillExists = request.getItems().stream()
                .anyMatch(req -> req.getProductVariantId().equals(
                        existingItem.getProductVariant().getProductVariantId()));
        if (!stillExists) {
            ProductVariant variant = existingItem.getProductVariant();
            variant.setQuantity(variant.getQuantity() - existingItem.getQuantity());
            productVariantRepository.save(variant);

            purchaseItemService.deletePurchaseItem(existingItem.getId());
            currentItems.remove(existingItem); // update working list
        }
    }

    // -------------------------
    // 4️⃣ Update existing items or add new ones
    // -------------------------
    for (var itemReq : request.getItems()) {
        PurchaseItem existingItem = currentItems.stream()
                .filter(i -> i.getProductVariant().getProductVariantId().equals(itemReq.getProductVariantId()))
                .findFirst().orElse(null);

        if (existingItem != null) {
            // ✅ Update quantity and costPrice, adjust stock by delta
            ProductVariant variant = existingItem.getProductVariant();
            int delta = itemReq.getQuantity() - existingItem.getQuantity();
            variant.setQuantity(variant.getQuantity() + delta);
            productVariantRepository.save(variant);

            existingItem.setQuantity(itemReq.getQuantity());
            existingItem.setCostPrice(itemReq.getCostPrice());
            purchaseItemRepository.save(existingItem); // save the updated item
        } else {
            // ✅ Add completely new item
            purchaseItemService.addPurchaseItem(
                    purchase.getId(),
                    itemReq.getProductVariantId(),
                    itemReq.getQuantity(),
                    itemReq.getCostPrice());
        }
    }

    // -------------------------
    // 5️⃣ Save purchase and return updated DTO
    // -------------------------
    purchaseRepository.save(purchase);
    return getPurchaseById(purchase.getId());
}

   
    @Transactional
    public void deletePurchase(Long id) {
        Purchase purchase = getPurchaseOrThrow(id);

        for (PurchaseItem item : purchase.getItems()) {
            ProductVariant variant = item.getProductVariant();
            variant.setQuantity(variant.getQuantity() - item.getQuantity());
            productVariantRepository.save(variant);
        }

        purchaseRepository.delete(purchase);
    }

    // -------------------------
    // Calculate total amount
    // -------------------------
    public BigDecimal calculateTotalAmount(Purchase purchase) {
        if (purchase.getItems() == null)
            return BigDecimal.ZERO;

        return purchase.getItems().stream()
                .map(item -> item.getCostPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

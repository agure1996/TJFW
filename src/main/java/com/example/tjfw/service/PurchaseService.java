package com.example.tjfw.service;

import com.example.tjfw.mapper.PurchaseMapper;
import com.example.tjfw.dto.purchase.PurchaseDTO;
import com.example.tjfw.dto.purchase.RequestPurchaseDTO;
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
    private final PurchaseMapper purchaseMapper;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           SupplierRepository supplierRepository,
                           PurchaseItemService purchaseItemService,
                           ProductVariantRepository productVariantRepository,
                           PurchaseItemRepository purchaseItemRepository,
                           PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseItemService = purchaseItemService;
        this.productVariantRepository = productVariantRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.purchaseMapper = purchaseMapper;
    }

    private Purchase getPurchaseOrThrow(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase with id " + id + " not found"));
    }

    private Supplier getSupplierOrThrow(Long id) {
        return supplierRepository.findBySupplierId(id)
                .orElseThrow(() -> new NotFoundException("Supplier with id " + id + " not found"));
    }

    @Transactional
    public PurchaseDTO createPurchase(RequestPurchaseDTO request) {
        Supplier supplier = getSupplierOrThrow(request.getSupplierId());

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setPurchaseType(request.getPurchaseType());
        purchase = purchaseRepository.save(purchase);

        if (request.getItems() != null) {
            for (var itemReq : request.getItems()) {
                purchaseItemService.addPurchaseItem(
                        purchase.getId(),
                        itemReq.getProductVariantId(),
                        itemReq.getQuantity(),
                        itemReq.getCostPrice());
            }
        }

        return purchaseMapper.toDTO(getPurchaseOrThrow(purchase.getId()));
    }

    public PurchaseDTO getPurchaseById(Long id) {
        return purchaseMapper.toDTO(getPurchaseOrThrow(id));
    }

    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(purchaseMapper::toDTO)
                .toList();
    }

    @Transactional
    public PurchaseDTO updatePurchase(Long id, RequestPurchaseDTO request) {
        Purchase purchase = getPurchaseOrThrow(id);

        if (request.getSupplierId() != null)
            purchase.setSupplier(getSupplierOrThrow(request.getSupplierId()));
        if (request.getPurchaseDate() != null)
            purchase.setPurchaseDate(request.getPurchaseDate());
        if (request.getPurchaseType() != null)
            purchase.setPurchaseType(request.getPurchaseType());

        List<PurchaseItem> currentItems = purchaseItemService.getAllItemsForPurchase(purchase.getId());

        // Remove items no longer in request
        for (PurchaseItem existingItem : new ArrayList<>(currentItems)) {
            boolean stillExists = request.getItems().stream()
                    .anyMatch(req -> req.getProductVariantId().equals(
                            existingItem.getProductVariant().getProductVariantId()));
            if (!stillExists) {
                ProductVariant variant = existingItem.getProductVariant();
                variant.setQuantity(variant.getQuantity() - existingItem.getQuantity());
                productVariantRepository.save(variant);
                purchaseItemService.deletePurchaseItem(existingItem.getId());
                currentItems.remove(existingItem);
            }
        }

        // Update existing or add new items
        for (var itemReq : request.getItems()) {
            PurchaseItem existingItem = currentItems.stream()
                    .filter(i -> i.getProductVariant().getProductVariantId()
                            .equals(itemReq.getProductVariantId()))
                    .findFirst().orElse(null);

            if (existingItem != null) {
                ProductVariant variant = existingItem.getProductVariant();
                int delta = itemReq.getQuantity() - existingItem.getQuantity();
                variant.setQuantity(variant.getQuantity() + delta);
                productVariantRepository.save(variant);
                existingItem.setQuantity(itemReq.getQuantity());
                existingItem.setCostPrice(itemReq.getCostPrice());
                purchaseItemRepository.save(existingItem);
            } else {
                purchaseItemService.addPurchaseItem(
                        purchase.getId(),
                        itemReq.getProductVariantId(),
                        itemReq.getQuantity(),
                        itemReq.getCostPrice());
            }
        }

        purchaseRepository.save(purchase);
        return purchaseMapper.toDTO(getPurchaseOrThrow(id));
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

    public BigDecimal calculateTotalAmount(Purchase purchase) {
        if (purchase.getItems() == null) return BigDecimal.ZERO;
        return purchase.getItems().stream()
                .map(item -> item.getCostPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
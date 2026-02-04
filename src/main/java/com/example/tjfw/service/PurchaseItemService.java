package com.example.tjfw.service;

import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.entity.Purchase;
import com.example.tjfw.entity.PurchaseItem;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductVariantRepository;
import com.example.tjfw.repository.PurchaseItemRepository;
import com.example.tjfw.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductVariantRepository productVariantRepository;

    public PurchaseItemService(PurchaseItemRepository purchaseItemRepository, PurchaseRepository purchaseRepository, ProductVariantRepository productVariantRepository) {
        this.purchaseItemRepository = purchaseItemRepository;
        this.purchaseRepository = purchaseRepository;
        this.productVariantRepository = productVariantRepository;
    }
    private PurchaseItem getPurchaseItemOrThrow(Long id) {
        return purchaseItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase item not found with id " + id));
    }

    private Purchase getPurchaseOrThrow(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found with id " + id));
    }

    private ProductVariant getVariantOrThrow(Long id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product variant not found with id " + id));
    }

    // Add single item to a purchase
    public PurchaseItem addPurchaseItem(Long purchaseId, Long variantId, int quantity, BigDecimal costPrice) {
        Purchase purchase = getPurchaseOrThrow(purchaseId);
        ProductVariant variant = getVariantOrThrow(variantId);

        PurchaseItem item = new PurchaseItem();
        item.setPurchase(purchase);
        item.setProductVariant(variant);
        item.setQuantity(quantity);
        item.setCostPrice(costPrice);

        return purchaseItemRepository.save(item);
    }

    public PurchaseItem getPurchaseItemById(Long id) {
        return getPurchaseItemOrThrow(id);
    }

    public void deletePurchaseItem(Long id) {
        PurchaseItem item = getPurchaseItemOrThrow(id);
        purchaseItemRepository.delete(item);
    }

    public List<PurchaseItem> getAllItemsForPurchase(Long purchaseId) {
        return purchaseItemRepository.findByPurchaseId(purchaseId);
    }
}

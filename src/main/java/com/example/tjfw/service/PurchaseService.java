package com.example.tjfw.service;

import com.example.tjfw.entity.Purchase;
import com.example.tjfw.entity.Supplier;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.PurchaseRepository;
import com.example.tjfw.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
    }

    private Purchase getPurchaseOrThrow(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase with id " + id + " not found"));
    }

    private Supplier getSupplierOrThrow(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier with id " + id + " not found"));
    }

    public Purchase createPurchase(Purchase purchase , Long supplierId) {
        //check if supplier exists
        Supplier supplier = getSupplierOrThrow(supplierId);
        //create a purchase from a supplier
        purchase.setSupplier(supplier);
        return purchaseRepository.save(purchase);
    }
    public Purchase getPurchaseById(Long id) {
        return getPurchaseOrThrow(id);
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase updatePurchase(Long id, Purchase updatePurchase){
        Purchase existingPurchase = getPurchaseOrThrow(id);
        existingPurchase.setPurchaseDate(updatePurchase.getPurchaseDate());
        if(updatePurchase.getSupplier() != null){
            existingPurchase.setSupplier(updatePurchase.getSupplier());
        }
        return purchaseRepository.save(existingPurchase);
    }

    public void deletePurchase(Long id) {
        Purchase purchase = getPurchaseOrThrow(id);
        purchaseRepository.delete(purchase);
    }

}

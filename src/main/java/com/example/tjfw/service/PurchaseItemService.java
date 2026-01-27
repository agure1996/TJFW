package com.example.tjfw.service;
import com.example.tjfw.repository.PurchaseItemRepository;
import org.springframework.stereotype.Service;

@Service
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;

    public PurchaseItemService(PurchaseItemRepository purchaseItemRepository) {this.purchaseItemRepository = purchaseItemRepository;}

}

package com.example.tjfw.service;
import com.example.tjfw.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {this.purchaseRepository = purchaseRepository;}

}

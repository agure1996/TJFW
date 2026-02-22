package com.example.tjfw.mapper;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.dto.purchase.PurchaseDTO;
import com.example.tjfw.dto.purchaseitem.PurchaseItemDTO;
import com.example.tjfw.dto.supplier.SupplierDTO;
import com.example.tjfw.entity.Purchase;
import com.example.tjfw.entity.PurchaseItem;
import com.example.tjfw.entity.Supplier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PurchaseMapper {

    public SupplierDTO toSupplierDTO(Supplier supplier) {
        return new SupplierDTO(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getSupplierContactInfo(),
                supplier.getNotes()
        );
    }

    public ProductVariantDTO toVariantDTO(PurchaseItem item) {
        return new ProductVariantDTO(
                item.getProductVariant().getProductVariantId(),
                item.getProductVariant().getProduct().getProductId(),
                item.getProductVariant().getProduct().getProductName(),
                item.getProductVariant().getColor(),
                item.getProductVariant().getSalePrice(),
                item.getProductVariant().getSize(),
                item.getProductVariant().getQuantity(),
                item.getProductVariant().getSku()
        );
    }

    public PurchaseItemDTO toItemDTO(PurchaseItem item) {
        return new PurchaseItemDTO(
                item.getId(),
                toVariantDTO(item),
                item.getQuantity(),
                item.getCostPrice()
        );
    }

    public PurchaseDTO toDTO(Purchase purchase) {
        List<PurchaseItemDTO> itemDTOs = purchase.getItems() == null
                ? List.of()
                : purchase.getItems().stream().map(this::toItemDTO).toList();

        BigDecimal totalAmount = purchase.getItems() == null
                ? BigDecimal.ZERO
                : purchase.getItems().stream()
                .map(i -> i.getCostPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PurchaseDTO(
                purchase.getId(),
                toSupplierDTO(purchase.getSupplier()),
                purchase.getPurchaseType(),
                purchase.getPurchaseDate(),
                totalAmount,
                itemDTOs
        );
    }
}
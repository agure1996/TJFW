package com.example.tjfw.mapper;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.dto.sale.SaleDTO;
import com.example.tjfw.dto.saleitem.SaleItemDTO;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.entity.Sale;
import com.example.tjfw.entity.SaleItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SaleMapper {

    public ProductVariantDTO toVariantDTO(ProductVariant variant) {
        return new ProductVariantDTO(
                variant.getProductVariantId(),
                variant.getProduct().getProductId(),
                variant.getProduct().getProductName(),
                variant.getColor(),
                variant.getSalePrice(),
                variant.getSize(),
                variant.getQuantity(),
                variant.getSku()
        );
    }

    public SaleItemDTO toItemDTO(SaleItem item) {
        return new SaleItemDTO(
                toVariantDTO(item.getProductVariant()),
                item.getQuantity(),
                item.getSalePrice()
        );
    }

    public SaleDTO toDTO(Sale sale) {
        List<SaleItemDTO> itemDTOs = sale.getItems() == null
                ? List.of()
                : sale.getItems().stream().map(this::toItemDTO).toList();

        BigDecimal totalAmount = sale.getTotalAmount();

        return new SaleDTO(
                sale.getId(),
                sale.getSaleDate(),
                itemDTOs,
                totalAmount,
                sale.getCustomerName(),
                sale.getCustomerContact()
        );
    }
}
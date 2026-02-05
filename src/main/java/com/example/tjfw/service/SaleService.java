package com.example.tjfw.service;

import com.example.tjfw.dto.productvariant.ProductVariantDTO;
import com.example.tjfw.dto.sale.SaleDTO;
import com.example.tjfw.dto.saleitem.SaleItemDTO;
import com.example.tjfw.dto.sale.RequestSaleDTO;
import com.example.tjfw.dto.saleitem.RequestSaleItemDTO;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.entity.Sale;
import com.example.tjfw.entity.SaleItem;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductVariantRepository;
import com.example.tjfw.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductVariantRepository variantRepository;

    public SaleService(SaleRepository saleRepository, ProductVariantRepository variantRepository) {
        this.saleRepository = saleRepository;
        this.variantRepository = variantRepository;
    }

    private ProductVariant getVariantOrThrow(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product variant not found"));
    }

    // -------------------------
    // Create a sale (handles stock reduction)
    // -------------------------
    public SaleDTO createSale(RequestSaleDTO request) {
        Sale sale = new Sale();
        sale.setSaleDate(request.getSaleDate());

        List<SaleItemDTO> itemDTOs = new ArrayList<>();
        List<SaleItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (RequestSaleItemDTO reqItem : request.getItems()) {
            ProductVariant variant = getVariantOrThrow(reqItem.getProductVariantId());

            if (variant.getQuantity() < reqItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for variant: " + variant.getSku());
            }

            variant.setQuantity(variant.getQuantity() - reqItem.getQuantity());
            variantRepository.save(variant);

            SaleItem saleItem = new SaleItem(sale, variant, reqItem.getQuantity(), reqItem.getSalePrice());
            items.add(saleItem);

            itemDTOs.add(new SaleItemDTO(
                    new ProductVariantDTO(
                            variant.getProductVariantId(),
                            variant.getProduct().getProductId(),
                            variant.getProduct().getProductName(),
                            variant.getColor(),
                            variant.getSalePrice(),
                            variant.getSize(),
                            variant.getQuantity(),
                            variant.getSku()
                    ),
                    saleItem.getQuantity(),
                    saleItem.getSalePrice()
            ));

            total = total.add(reqItem.getSalePrice().multiply(BigDecimal.valueOf(reqItem.getQuantity())));
        }

        sale.setItems(items);
        sale.setTotalAmount(total);
        saleRepository.save(sale);

        return new SaleDTO(sale.getId(), sale.getSaleDate(), itemDTOs, total);
    }

    // -------------------------
    // Map a Sale entity to SaleDTO
    // -------------------------
    public SaleDTO createSaleDTOFromEntity(Sale sale) {
        List<SaleItemDTO> itemDTOs = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (sale.getItems() != null) {
            for (SaleItem item : sale.getItems()) {
                ProductVariant variant = item.getProductVariant();

                ProductVariantDTO variantDTO = new ProductVariantDTO(
                        variant.getProductVariantId(),
                        variant.getProduct().getProductId(),
                        variant.getProduct().getProductName(),
                        variant.getColor(),
                        variant.getSalePrice(),
                        variant.getSize(),
                        variant.getQuantity(),
                        variant.getSku()
                );

                SaleItemDTO saleItemDTO = new SaleItemDTO(
                        variantDTO,
                        item.getQuantity(),
                        item.getSalePrice()
                );

                itemDTOs.add(saleItemDTO);
                totalAmount = totalAmount.add(item.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        return new SaleDTO(
                sale.getId(),
                sale.getSaleDate(),
                itemDTOs,
                totalAmount
        );
    }

    // -------------------------
    // Fetch all sales
    // -------------------------
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    // -------------------------
    // Fetch sale by id
    // -------------------------
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale not found"));
    }
}

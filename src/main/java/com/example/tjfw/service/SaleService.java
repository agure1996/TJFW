package com.example.tjfw.service;

import com.example.tjfw.mapper.SaleMapper;
import com.example.tjfw.dto.sale.RequestSaleDTO;
import com.example.tjfw.dto.sale.SaleDTO;
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
    private final SaleMapper saleMapper;

    public SaleService(SaleRepository saleRepository,
                       ProductVariantRepository variantRepository,
                       SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.variantRepository = variantRepository;
        this.saleMapper = saleMapper;
    }

    private ProductVariant getVariantOrThrow(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product variant not found"));
    }

    public SaleDTO createSale(RequestSaleDTO request) {
        Sale sale = new Sale();
        sale.setSaleDate(request.getSaleDate());
        sale.setCustomerName(request.getCustomerName());
        sale.setCustomerContact(request.getCustomerContact());

        List<SaleItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (RequestSaleItemDTO reqItem : request.getItems()) {
            ProductVariant variant = getVariantOrThrow(reqItem.getProductVariantId());

            if (variant.getQuantity() < reqItem.getQuantity())
                throw new IllegalArgumentException("Insufficient stock for variant: " + variant.getSku());

            variant.setQuantity(variant.getQuantity() - reqItem.getQuantity());
            variantRepository.save(variant);

            items.add(new SaleItem(sale, variant, reqItem.getQuantity(), reqItem.getSalePrice()));
            total = total.add(reqItem.getSalePrice().multiply(BigDecimal.valueOf(reqItem.getQuantity())));
        }

        sale.setItems(items);
        sale.setTotalAmount(total);
        saleRepository.save(sale);

        return saleMapper.toDTO(sale);
    }

    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll().stream()
                .map(saleMapper::toDTO)
                .toList();
    }

    public SaleDTO getSaleById(Long id) {
        return saleMapper.toDTO(saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale not found")));
    }

    public SaleDTO updateSale(Long saleId, RequestSaleDTO request) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new NotFoundException("Sale not found"));

        // Restore stock for old items
        if (sale.getItems() != null) {
            for (SaleItem item : sale.getItems()) {
                ProductVariant variant = item.getProductVariant();
                variant.setQuantity(variant.getQuantity() + item.getQuantity());
                variantRepository.save(variant);
            }
        }

        sale.getItems().clear();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (RequestSaleItemDTO reqItem : request.getItems()) {
            ProductVariant variant = getVariantOrThrow(reqItem.getProductVariantId());

            if (variant.getQuantity() < reqItem.getQuantity())
                throw new IllegalArgumentException("Insufficient stock for variant: " + variant.getSku());

            variant.setQuantity(variant.getQuantity() - reqItem.getQuantity());
            variantRepository.save(variant);

            SaleItem saleItem = new SaleItem();
            saleItem.setSale(sale);
            saleItem.setProductVariant(variant);
            saleItem.setQuantity(reqItem.getQuantity());
            saleItem.setSalePrice(reqItem.getSalePrice());
            sale.getItems().add(saleItem);

            totalAmount = totalAmount.add(reqItem.getSalePrice()
                    .multiply(BigDecimal.valueOf(reqItem.getQuantity())));
        }

        sale.setSaleDate(request.getSaleDate());
        sale.setCustomerName(request.getCustomerName());
        sale.setCustomerContact(request.getCustomerContact());
        sale.setTotalAmount(totalAmount);
        saleRepository.save(sale);

        return saleMapper.toDTO(sale);
    }

    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale not found"));

        if (sale.getItems() != null) {
            for (SaleItem item : sale.getItems()) {
                ProductVariant variant = item.getProductVariant();
                variant.setQuantity(variant.getQuantity() + item.getQuantity());
                variantRepository.save(variant);
            }
        }

        saleRepository.delete(sale);
    }
}
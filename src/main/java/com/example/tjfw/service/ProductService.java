package com.example.tjfw.service;

import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.Supplier;
import com.example.tjfw.exceptions.AlreadyExistsException;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierService supplierService;

    public ProductService(ProductRepository productRepository, SupplierService supplierService) {
        this.productRepository = productRepository;
        this.supplierService = supplierService;
    }

    public Product findById(Long id) {
        return getProductOrThrow(id);
    }

    //Created to stop code duplication
    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product createNewProduct(Product product) {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new AlreadyExistsException("Product already exists");
        }
        return productRepository.save(product);
    }

    public Product createNewProduct(Product product, Long supplierId) {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new AlreadyExistsException("Product already exists");
        }
        if (supplierId != null) {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            product.setSupplier(supplier);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct, Long supplierId) {
        Product existing = getProductOrThrow(id);
        
        existing.setProductName(updatedProduct.getProductName());
        existing.setProductType(updatedProduct.getProductType());
        existing.setDescription(updatedProduct.getProductDescription());
        
        if (supplierId != null) {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            existing.setSupplier(supplier);
        }
        
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        Product product = getProductOrThrow(id);
        productRepository.delete(product);
    }
}

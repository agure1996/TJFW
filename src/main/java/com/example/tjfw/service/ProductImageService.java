package com.example.tjfw.service;

import com.example.tjfw.mapper.ProductMapper;
import com.example.tjfw.dto.productimage.PhotoUploadResult;
import com.example.tjfw.dto.productimage.ProductImageDTO;
import com.example.tjfw.entity.Product;
import com.example.tjfw.entity.ProductImage;
import com.example.tjfw.entity.ProductVariant;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ProductImageService {

    private final ProductImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;
    private final ProductService productService;
    private final ProductVariantService variantService;
    private final ProductMapper productMapper;

    public ProductImageService(ProductImageRepository imageRepository,
                               CloudinaryService cloudinaryService,
                               ProductService productService,
                               ProductVariantService variantService,
                               ProductMapper productMapper) {
        this.imageRepository = imageRepository;
        this.cloudinaryService = cloudinaryService;
        this.productService = productService;
        this.variantService = variantService;
        this.productMapper = productMapper;
    }

    public ProductImageDTO uploadImage(Long productId, Long variantId, MultipartFile file) throws IOException {
        Product product = null;
        ProductVariant variant = null;

        if (productId != null) product = productService.findById(productId);
        if (variantId != null) variant = variantService.findProductVariantById(variantId);

        if (product == null && variant == null)
            throw new IllegalArgumentException("Image must belong to either product or variant");
        if (product != null && variant != null)
            throw new IllegalArgumentException("Image cannot belong to both product and variant");

        String folder = (product != null)
                ? "tjfw/products/" + product.getProductId()
                : "tjfw/variants/" + variant.getProductVariantId();

        PhotoUploadResult result = cloudinaryService.uploadImage(file, folder);

        List<ProductImage> currentImages = (product != null)
                ? imageRepository.findByProductIdOrderByDisplayOrderAsc(product.getProductId())
                : imageRepository.findByVariantIdOrderByDisplayOrderAsc(variant.getProductVariantId());

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setProductVariant(variant);
        image.setImageUrl(result.url());
        image.setPublicId(result.publicId());
        image.setIsMain(currentImages.isEmpty());
        image.setDisplayOrder(currentImages.size());
        image.setAltText(file.getOriginalFilename());

        return productMapper.toImageDTO(imageRepository.save(image));
    }

    public void deleteImage(Long imageId, Long productId, Long variantId) throws IOException {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));

        boolean belongsToProduct = image.getProduct() != null
                && productId != null
                && image.getProduct().getProductId().equals(productId);
        boolean belongsToVariant = image.getProductVariant() != null
                && variantId != null
                && image.getProductVariant().getProductVariantId().equals(variantId);

        if (!belongsToProduct && !belongsToVariant)
            throw new IllegalArgumentException("Image does not belong to the specified product/variant");

        List<ProductImage> otherImages = (image.getProduct() != null)
                ? imageRepository.findByProductIdOrderByDisplayOrderAsc(image.getProduct().getProductId())
                : imageRepository.findByVariantIdOrderByDisplayOrderAsc(image.getProductVariant().getProductVariantId());

        otherImages = otherImages.stream()
                .filter(img -> !img.getImageId().equals(imageId))
                .toList();

        if (image.getIsMain() && !otherImages.isEmpty())
            throw new IllegalArgumentException("Cannot delete main image. Set another image as main first.");

        cloudinaryService.deleteImage(image.getPublicId());
        imageRepository.delete(image);
    }

    public ProductImageDTO setMainImage(Long imageId, Long productId, Long variantId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));

        boolean belongsToProduct = image.getProduct() != null
                && productId != null
                && image.getProduct().getProductId().equals(productId);
        boolean belongsToVariant = image.getProductVariant() != null
                && variantId != null
                && image.getProductVariant().getProductVariantId().equals(variantId);

        if (!belongsToProduct && !belongsToVariant)
            throw new IllegalArgumentException("Image does not belong to the specified product/variant");

        List<ProductImage> allImages = (image.getProduct() != null)
                ? imageRepository.findByProductIdOrderByDisplayOrderAsc(image.getProduct().getProductId())
                : imageRepository.findByVariantIdOrderByDisplayOrderAsc(image.getProductVariant().getProductVariantId());

        allImages.forEach(img -> img.setIsMain(false));
        imageRepository.saveAll(allImages);

        image.setIsMain(true);
        return productMapper.toImageDTO(imageRepository.save(image));
    }

    public List<ProductImageDTO> getProductImages(Long productId, Long variantId) {
        List<ProductImage> images;
        if (productId != null) {
            productService.findById(productId);
            images = imageRepository.findByProductIdOrderByDisplayOrderAsc(productId);
        } else if (variantId != null) {
            variantService.findProductVariantById(variantId);
            images = imageRepository.findByVariantIdOrderByDisplayOrderAsc(variantId);
        } else {
            throw new IllegalArgumentException("Must provide either productId or variantId");
        }
        return images.stream().map(productMapper::toImageDTO).toList();
    }
}
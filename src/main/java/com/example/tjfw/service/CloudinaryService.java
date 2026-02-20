package com.example.tjfw.service;

import com.example.tjfw.dto.productimage.PhotoUploadResult;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public PhotoUploadResult uploadImage(MultipartFile file, String folder) throws IOException {
        Map<String, Object> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folder,
                        "resource_type", "image",
                        "transformation", ObjectUtils.asMap(
                                "width", 800,
                                "height", 800,
                                "crop", "limit",
                                "quality", "auto"
                        )
                )
        );

        String url = result.get("secure_url").toString();
        String publicId = result.get("public_id").toString();

        return new PhotoUploadResult(url, publicId);
    }

    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
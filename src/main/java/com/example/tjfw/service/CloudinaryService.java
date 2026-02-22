package com.example.tjfw.service;

import com.cloudinary.Transformation;
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

        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folder,
                        "resource_type", "image",
                        "transformation", new Transformation<>()
                                .width(800)
                                .height(800)
                                .crop("limit")
                                .quality("auto")
                )
        );

        Object secureUrlObj = result.get("secure_url");
        Object publicIdObj = result.get("public_id");

        if (secureUrlObj == null || publicIdObj == null) {
            throw new RuntimeException("Cloudinary did not return secure_url or public_id");
        }

        String url = secureUrlObj.toString();
        String publicId = publicIdObj.toString();
        System.out.println("Cloudinary upload result: " + result);

        return new PhotoUploadResult(url, publicId);
    }

    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
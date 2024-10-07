package com.pago.dotodo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class CloudService {

    private String IMG_FOLDER = "https://res.cloudinary.com/";

    private final Cloudinary cloudinary;

    public CloudService(@Value("${cloudinady.api-key}") String key,
                        @Value("${cloudinady.api-secret}") String secret,
                        @Value("${cloudinady.cloud-name}") String name) {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", name,
                "api_key", key,
                "api_secret", secret,
                "secure", true));

        IMG_FOLDER += name + "/image/upload/v1723887759/";
    }

    public String saveImage(MultipartFile image) {
        String imageId = UUID.randomUUID().toString();

        Map params = ObjectUtils.asMap(
                "public_id", imageId,
                "overwrite", true,
                "resource_type", "image"
        );

        File tmpFile = new File(imageId);
        try {
            Files.write(tmpFile.toPath(), image.getBytes());
            cloudinary.uploader().upload(tmpFile, params);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
        }

        return String.format(IMG_FOLDER + imageId + "." + getFileExtension(Objects.requireNonNull(image.getOriginalFilename())));
    }

    private String getFileExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
}

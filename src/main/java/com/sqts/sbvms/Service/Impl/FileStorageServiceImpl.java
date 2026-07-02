package com.sqts.sbvms.Service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sqts.sbvms.Exception.InvalidFileException;
import com.sqts.sbvms.Service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Cloudinary cloudinary;

    public FileStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(
            MultipartFile file,
            String folder) {

        if (file == null || file.isEmpty())
            throw new InvalidFileException(
                    "Please upload a valid file.");

        try {

            String publicId =
                    folder + "/" + UUID.randomUUID();

            Map<?, ?> uploadResult =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "public_id",
                                    publicId,
                                    "resource_type",
                                    "auto"
                            )
                    );

            return uploadResult.get("secure_url").toString();

        } catch (IOException ex) {

            throw new InvalidFileException(
                    "Unable to upload file.");
        }
    }
}
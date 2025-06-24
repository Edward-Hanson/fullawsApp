package com.hanson.cloudbasedproject.cloudbasedproject.service;

import com.hanson.cloudbasedproject.cloudbasedproject.dtos.ImageTransfer;
import com.hanson.cloudbasedproject.cloudbasedproject.entities.ImageMetadata;
import com.hanson.cloudbasedproject.cloudbasedproject.repository.ImageMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Client s3Client;
    private final ImageMetadataRepository imageRepo;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    public void uploadFile(MultipartFile[] files) throws IOException {
        List<ImageMetadata> metadataList = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            String url = s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(filename).build()).toString();
            metadataList.add(new ImageMetadata(filename, url));
        }
        imageRepo.saveAll(metadataList);
    }

    public Page<?> home(int page){
        long count = imageRepo.count();
        int totalPages = (int) Math.ceil((double) count / 6.0);
        int safePage = (page < 0 || page >= totalPages) ? 0 : page;
        return imageRepo.findAll(PageRequest.of(safePage,6));
    }

    public ImageTransfer downloadImage(Long id) throws IOException {
        ImageMetadata meta = imageRepo.findById(id)
                .orElseThrow(() -> new FileNotFoundException("Image not found"));

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(meta.getFilename())
                .build());

        return new ImageTransfer(objectBytes.asByteArray(),meta.getFilename());
    }

    public InputStreamResource renderImage( Long id) throws  IOException {
        ImageMetadata meta = imageRepo.findById(id).orElseThrow(() -> new FileNotFoundException("Image not found"));

        ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(meta.getFilename())
                        .build());
        return new InputStreamResource(stream);
    }

    public void deleteImage(Long id) throws IOException {
        ImageMetadata meta = imageRepo.findById(id)
                .orElseThrow(() -> new FileNotFoundException("Image not found"));

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(meta.getFilename())
                .build());

        imageRepo.delete(meta);
    }
}

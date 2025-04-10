package com.example.video.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.video.dto.DeleteRequestDTO;
import com.example.video.dto.UploadRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3service {

    private final AmazonS3 amazonS3;

    @Value("${BUCKETNAME}")
    private String bucket;

    @Value("${REGION}")
    private String region;

    public String upload(@ModelAttribute UploadRequestDTO request) throws IOException {

        String fileName = request.getFile().getOriginalFilename();
        String key = request.getUserName() + "/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(request.getFile().getContentType());
        metadata.setContentLength(request.getFile().getSize());

        amazonS3.putObject(bucket, key, request.getFile().getInputStream(), metadata);

        String fileUrl = "https://" + bucket + ".s3.amazonaws.com/" + key;

        return fileUrl;
    }

    public void delete(DeleteRequestDTO request){
        String bucketUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
        String imageUrl=request.getImageUrl();

        if (!imageUrl.startsWith(bucketUrl)) {
            throw new IllegalArgumentException("Invalid S3 image URL.");
        }

        String objectKey = imageUrl.substring(bucketUrl.length());

        amazonS3.deleteObject(bucket, objectKey);
    }
}

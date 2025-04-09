package com.example.video.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3service {

    private final AmazonS3 amazonS3;

    @Value("${BUCKETNAME}")
    private String bucket;

    public String upload(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        String key = "test/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(bucket, key, file.getInputStream(), metadata);

        String fileUrl = "https://" + bucket + ".s3.amazonaws.com/" + key;

        return fileUrl;
    }
}

package com.example.video.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.video.dto.DeleteRequestDTO;
import com.example.video.dto.ReadUserImageDTO;
import com.example.video.dto.UploadRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> readUserImage(ReadUserImageDTO request){
        List<String>url=new ArrayList<>();

        String prefix = request.getUserName() + "/";
        ListObjectsV2Request listRequest = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(prefix);

        return amazonS3.listObjectsV2(listRequest).getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .map(key -> "https://" + bucket + ".s3.amazonaws.com/" + key)
                .collect(Collectors.toList());
    }
}

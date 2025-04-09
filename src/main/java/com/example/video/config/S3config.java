package com.example.video.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3config {

    @Value("${S3ACCESSKEY}")
    private String accessKey;

    @Value("${S3SECRETKEY}")
    private String secretKey;

    @Value("${REGION}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        System.out.println("AccessKey: " + accessKey);
        System.out.println("Region: " + region);

        if (accessKey == null || secretKey == null || region == null) {
            throw new IllegalArgumentException("AWS credentials or region not configured properly!");
        }

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

}

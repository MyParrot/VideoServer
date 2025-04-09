package com.example.video.controller;

import com.example.video.global.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.video.service.S3service;
import java.io.IOException;

@RestController
@RequestMapping("/s3")
public class S3controller {

    @Autowired
    private S3service s3service;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String imageUrl = s3service.upload(file);
        return ResponseEntity.ok(new ResponseDto<>(imageUrl));
    }
}

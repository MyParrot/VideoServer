package com.example.video.controller;

import com.example.video.dto.UploadRequestDTO;
import com.example.video.global.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.video.service.S3service;
import java.io.IOException;

@RestController
@RequestMapping("/s3")
public class S3controller {

    @Autowired
    private S3service s3service;


    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@ModelAttribute UploadRequestDTO request) throws IOException {
        String imageUrl = s3service.upload(request);
        return ResponseEntity.ok(new ResponseDto<>(imageUrl));
    }
    

}

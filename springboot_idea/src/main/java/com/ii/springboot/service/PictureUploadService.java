package com.ii.springboot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PictureUploadService {

    public String resolve(MultipartFile file) {
        String s = file.getOriginalFilename();

        return s;
    }
}

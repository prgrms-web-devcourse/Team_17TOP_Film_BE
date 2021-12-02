package com.programmers.film.img.controller;

import com.programmers.film.img.S3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ImgUploadController { //테스트를 위한 controller로 개시물 생성 api에서 service만 사용하면 됨

    private final S3Service s3Service;

    @PostMapping("/api/v1/upload")
    public String uploadImage(@RequestPart MultipartFile file) throws IOException {
        return s3Service.upload(file);
    }
}

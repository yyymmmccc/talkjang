package com.talkjang.demo.controller;

import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("")
    public CommonResponseDto upload(@RequestParam("files") MultipartFile[] files) throws IOException {

        return uploadService.upload(files);
    }
}

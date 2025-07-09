package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.handler.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UploadService {

    String FILE_DIR = "/Users/myungchul-yoon/talkjang_upload/images/";

    public CommonResponseDto upload(MultipartFile[] files) throws IOException {

        List<String> newFileList = new ArrayList<>();

        for(MultipartFile file : files){

            String fileType = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf(".")+1);

            validateFileType(fileType);

            String newFileName = UUID.randomUUID() + "." + fileType;

            // 새로운이미지이름을 만들어 로컬에 저장
            file.transferTo(new File(FILE_DIR, newFileName));

            // 프론트에서 로컬에 요청할 수는 없으므로 서버 url
            newFileList.add("http://localhost:8080/image/" + newFileName);
        }

        return CommonResponseDto.success(newFileList);
    }

    public void validateFileType(String fileType){

        Set<String> types = Set.of("jpg", "jpeg", "png");

        if(!types.contains(fileType.toLowerCase())){
            throw new CustomException(ErrorCode.NOT_IMAGE_FILE);
        }
    }
}

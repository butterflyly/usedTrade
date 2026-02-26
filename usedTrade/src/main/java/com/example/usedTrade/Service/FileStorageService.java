package com.example.usedTrade.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.boardImagePath}")
    private String uploadFolder; // E:/upload/itemImages/

    // 파일 저장
    public String save(MultipartFile file) {

        try {
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID() + "_" + originalFilename;

            File target = new File(uploadFolder + filename);
            file.transferTo(target);

            // ✅ 외부에서 사용할 URL 리턴
            return "/itemImages/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }


    // 파일 삭제
    public void delete(String imageUrl) {
        String filename = imageUrl.replace("/itemImages/", "");
        File file = new File(uploadFolder + filename);

        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("파일 삭제 실패");
            }
        }
    }
}

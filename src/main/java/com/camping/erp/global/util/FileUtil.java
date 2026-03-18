package com.camping.erp.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class FileUtil {

    @Value("${file.upload-dir:./upload/}")
    private String uploadDir;

    /**
     * 파일 업로드 (UUID 생성 및 물리적 저장)
     */
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        String originFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + originFileName;
        Path filePath = Paths.get(uploadDir + fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return fileName;
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", fileName, e);
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 물리적 파일 삭제
     */
    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isBlank()) return;

        Path filePath = Paths.get(uploadDir + fileName);
        try {
            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                log.info("물리적 파일 삭제 성공: {}", fileName);
            } else {
                log.warn("삭제할 파일이 존재하지 않습니다: {}", fileName);
            }
        } catch (IOException e) {
            log.error("물리적 파일 삭제 중 오류 발생: {}", fileName, e);
            // 파일 삭제 실패는 비즈니스 로직 중단을 일으키지 않도록 로그만 기록
        }
    }
}

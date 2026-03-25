package com.camping.erp.global.config;

import com.camping.erp.domain.image.ImageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MigrationConfig {

    private final ImageRepository imageRepository;

    /**
     * 서버 기동 시 1회 실행: 기존에 잘못 저장된 이미지 경로(/upload/파일명)를 
     * 엔티티 설계에 맞게 경로(/upload/)만 남도록 일괄 교정합니다.
     */
    @PostConstruct
    public void migrateImagePaths() {
        try {
            log.info("이미지 경로 데이터 전수 마이그레이션(Static Images) 시작...");
            imageRepository.migrateAllToStaticImages();
            log.info("이미지 경로 데이터 전수 마이그레이션 완료.");
        } catch (Exception e) {
            log.error("이미지 경로 마이그레이션 중 오류 발생: {}", e.getMessage());
        }
    }
}

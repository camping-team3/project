package com.camping.erp.domain.gallery;

import com.camping.erp.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final FileUtil fileUtil;

    public Page<GalleryResponse.ListDTO> findAll(Pageable pageable, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return galleryRepository.findAllOrderByIdDesc(pageable)
                    .map(GalleryResponse.ListDTO::new);
        }
        return galleryRepository.findByKeywordOrderByIdDesc(keyword, pageable)
                .map(GalleryResponse.ListDTO::new);
    }

    public GalleryResponse.DetailDTO findById(Long id) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("갤러리 게시글을 찾을 수 없습니다."));
        return new GalleryResponse.DetailDTO(gallery);
    }

    // 사용자용 상세 조회 (조회수 증가 포함)
    @Transactional
    public GalleryResponse.DetailDTO findByIdWithViewCount(Long id) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("갤러리 게시글을 찾을 수 없습니다."));
        gallery.increaseViewCount();
        return new GalleryResponse.DetailDTO(gallery);
    }

    @Transactional
    public void save(GalleryRequest.SaveDTO saveDTO) {
        Gallery gallery = Gallery.builder()
                .title(saveDTO.getTitle())
                .category(saveDTO.getCategory())
                .shootingDate(saveDTO.getShootingDate())
                .content(saveDTO.getContent())
                .build();

        if (saveDTO.getImages() != null && !saveDTO.getImages().isEmpty()) {
            saveGalleryImages(gallery, saveDTO.getImages());
        }

        galleryRepository.save(gallery);
    }

    @Transactional
    public void update(Long id, GalleryRequest.UpdateDTO updateDTO) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("갤러리 게시글을 찾을 수 없습니다."));

        gallery.update(updateDTO.getTitle(), updateDTO.getCategory(), updateDTO.getShootingDate(),
                updateDTO.getContent());

        // 최종 이미지 개수 검증 (기존 - 삭제 + 신규)
        int currentCount = gallery.getImages().size();
        int deleteCount = (updateDTO.getDeleteImageIds() != null) ? updateDTO.getDeleteImageIds().size() : 0;
        int newCount = (updateDTO.getImages() != null)
                ? (int) updateDTO.getImages().stream().filter(img -> !img.isEmpty()).count()
                : 0;

        if (currentCount - deleteCount + newCount < 1) {
            throw new RuntimeException("갤러리 게시글에는 최소 한 장 이상의 사진이 있어야 합니다.");
        }

        // 물리적 이미지 파일 삭제 로직 추가
        if (updateDTO.getDeleteImageIds() != null && !updateDTO.getDeleteImageIds().isEmpty()) {
            gallery.getImages().stream()
                    .filter(img -> updateDTO.getDeleteImageIds().contains(img.getId()))
                    .forEach(img -> fileUtil.deleteFile(img.getFileName()));

            gallery.getImages().removeIf(img -> updateDTO.getDeleteImageIds().contains(img.getId()));
        }

        // 새로운 이미지 추가 저장
        if (updateDTO.getImages() != null && !updateDTO.getImages().isEmpty()) {
            saveGalleryImages(gallery, updateDTO.getImages());
        }
    }

    @Transactional
    public void delete(Long id) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("갤러리 게시글을 찾을 수 없습니다."));

        // 연관된 물리 이미지 파일들 모두 삭제
        if (gallery.getImages() != null && !gallery.getImages().isEmpty()) {
            gallery.getImages().forEach(img -> fileUtil.deleteFile(img.getFileName()));
        }

        galleryRepository.delete(gallery);
    }

    private void saveGalleryImages(Gallery gallery, List<MultipartFile> images) {
        for (MultipartFile file : images) {
            if (file.isEmpty())
                continue;

            String fileName = fileUtil.uploadFile(file);
            if (fileName != null) {
                com.camping.erp.domain.image.Image image = com.camping.erp.domain.image.Image.builder()
                        .fileName(fileName)
                        .filePath("/upload/" + fileName)
                        .build();
                gallery.addImage(image);
            }
        }
    }
}

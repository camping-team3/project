package com.camping.erp.domain.gallery;

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

    @Value("${file.upload-dir:./upload/}")
    private String uploadDir;

    public Page<GalleryResponse.ListDTO> findAll(Pageable pageable) {
        return galleryRepository.findAllOrderByIdDesc(pageable)
                .map(GalleryResponse.ListDTO::new);
    }

    public GalleryResponse.DetailDTO findById(Long id) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("갤러리 게시글을 찾을 수 없습니다."));
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
        
        gallery.update(updateDTO.getTitle(), updateDTO.getCategory(), updateDTO.getShootingDate(), updateDTO.getContent());

        // 기존 이미지 삭제 처리
        if (updateDTO.getDeleteImageIds() != null && !updateDTO.getDeleteImageIds().isEmpty()) {
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
        galleryRepository.delete(gallery);
    }

    private void saveGalleryImages(Gallery gallery, List<MultipartFile> images) {
        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;

            String originFileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + originFileName;
            Path filePath = Paths.get(uploadDir + fileName);

            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                com.camping.erp.domain.image.Image image = com.camping.erp.domain.image.Image.builder()
                        .fileName(fileName)
                        .filePath("/upload/" + fileName)
                        .build();
                gallery.addImage(image);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
            }
        }
    }
}

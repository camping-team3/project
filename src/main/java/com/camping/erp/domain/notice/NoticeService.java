package com.camping.erp.domain.notice;

import com.camping.erp.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Value;
>>>>>>> dev
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
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final FileUtil fileUtil;

<<<<<<< HEAD
=======
    @Value("${file.upload-dir:./upload/}")
    private String uploadDir;

>>>>>>> dev
    public Page<NoticeResponse.ListDTO> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return noticeRepository.findAllOrderByIsTopDescAndIdDesc(pageable)
                    .map(NoticeResponse.ListDTO::new);
        }
        return noticeRepository.findAllByKeyword(keyword, pageable)
                .map(NoticeResponse.ListDTO::new);
    }

    public NoticeResponse.DetailDTO findById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        return new NoticeResponse.DetailDTO(notice);
    }

    @Transactional
    public Long save(NoticeRequest.SaveDTO saveDTO) {
        Notice notice = saveDTO.toEntity();

        if (saveDTO.getImages() != null && !saveDTO.getImages().isEmpty()) {
            saveNoticeImages(notice, saveDTO.getImages());
        }

        Notice savedNotice = noticeRepository.save(notice);
        return savedNotice.getId();
    }

    @Transactional
    public void update(Long id, NoticeRequest.UpdateDTO updateDTO) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        
        // isTop이 null로 넘어올 경우(체크박스 해제 시) false로 처리
        boolean isTop = updateDTO.getIsTop() != null && updateDTO.getIsTop();
        notice.update(updateDTO.getTitle(), updateDTO.getContent(), isTop);

<<<<<<< HEAD
        // 물리적 이미지 파일 삭제 로직 추가
        if (updateDTO.getDeleteImageIds() != null && !updateDTO.getDeleteImageIds().isEmpty()) {
            notice.getImages().stream()
                    .filter(img -> updateDTO.getDeleteImageIds().contains(img.getId()))
                    .forEach(img -> fileUtil.deleteFile(img.getFileName()));
            
=======
        // 기존 이미지 삭제 처리
        if (updateDTO.getDeleteImageIds() != null && !updateDTO.getDeleteImageIds().isEmpty()) {
>>>>>>> dev
            notice.getImages().removeIf(img -> updateDTO.getDeleteImageIds().contains(img.getId()));
        }

        if (updateDTO.getImages() != null && !updateDTO.getImages().isEmpty()) {
            saveNoticeImages(notice, updateDTO.getImages());
        }
    }

<<<<<<< HEAD
    @Transactional
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        
        // 연관된 물리 이미지 파일들 모두 삭제
        if (notice.getImages() != null && !notice.getImages().isEmpty()) {
            notice.getImages().forEach(img -> fileUtil.deleteFile(img.getFileName()));
        }
        
        noticeRepository.delete(notice);
    }

=======
>>>>>>> dev
    /**
     * 공지사항 이미지 저장 공통 로직
     */
    private void saveNoticeImages(Notice notice, List<MultipartFile> images) {
        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;

<<<<<<< HEAD
            String fileName = fileUtil.uploadFile(file);
            if (fileName != null) {
=======
            String originFileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + originFileName;
            Path filePath = Paths.get(uploadDir + fileName);

            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

>>>>>>> dev
                com.camping.erp.domain.image.Image image = com.camping.erp.domain.image.Image.builder()
                        .fileName(fileName)
                        .filePath("/upload/" + fileName)
                        .build();
                notice.addImage(image);
<<<<<<< HEAD
            }
        }
=======
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        noticeRepository.delete(notice);
>>>>>>> dev
    }
}

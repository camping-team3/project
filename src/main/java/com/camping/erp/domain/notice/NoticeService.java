package com.camping.erp.domain.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

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
        Notice savedNotice = noticeRepository.save(notice);
        return savedNotice.getId();
    }

    @Transactional
    public void update(Long id, NoticeRequest.UpdateDTO updateDTO) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        notice.update(updateDTO.getTitle(), updateDTO.getContent(), updateDTO.getIsTop());
    }

    @Transactional
    public void delete(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        noticeRepository.delete(notice);
    }
}

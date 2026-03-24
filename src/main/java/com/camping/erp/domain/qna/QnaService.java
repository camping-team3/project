package com.camping.erp.domain.qna;

import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.UserResponse;
import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception403;
import com.camping.erp.global.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {
    private final QnaRepository qnaRepository;
    private final CommentRepository commentRepository;

    // 통계 데이터 조회
    public Map<String, Long> getStatistics() {
        Map<String, Long> stats = new HashMap<>();
        
        // 1. 미처리 문의 수
        stats.put("unansweredCount", qnaRepository.countUnanswered());
        
        // 2. 오늘 완료된 답변 수
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        stats.put("todayAnsweredCount", commentRepository.countTodayComments(startOfDay));
        
        return stats;
    }

    // 전체 목록 조회 (페이징 및 필터링 포함)
    public QnaResponse.PageDTO findAll(String status, UserResponse.LoginDTO sessionUser, Pageable pageable) {
        Page<Qna> page;
        if ("pending".equalsIgnoreCase(status)) {
            page = qnaRepository.findByIsAnsweredWithUser(false, pageable);
        } else if ("completed".equalsIgnoreCase(status)) {
            page = qnaRepository.findByIsAnsweredWithUser(true, pageable);
        } else {
            page = qnaRepository.findAllWithUser(pageable);
        }
        
        List<QnaResponse.ListDTO> dtoList = page.getContent().stream()
                .map(qna -> new QnaResponse.ListDTO(qna, sessionUser))
                .toList();

        int totalPages = page.getTotalPages();
        int currentPage = page.getNumber();

        // 하단 페이지 번호 5개씩 노출 로직
        int startPage = Math.max(0, (currentPage / 5) * 5);
        int endPage = Math.min(startPage + 4, totalPages - 1);

        List<QnaResponse.PageNumberDTO> pageNumbers = IntStream.rangeClosed(startPage, endPage)
                .mapToObj(n -> QnaResponse.PageNumberDTO.builder()
                        .number(n)
                        .displayDigit(n + 1)
                        .isCurrent(n == currentPage)
                        .build())
                .toList();

        return QnaResponse.PageDTO.builder()
                .qnas(dtoList)
                .status(status)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalElements(page.getTotalElements())
                .pageNumbers(pageNumbers)
                .hasPrev(page.hasPrevious())
                .hasNext(page.hasNext())
                .prevPage(currentPage - 1)
                .nextPage(currentPage + 1)
                .build();
    }

    // 상세 조회
    @Transactional
    public QnaResponse.DetailDTO findById(Long id, UserResponse.LoginDTO sessionUser) {
        Qna qna = qnaRepository.findByIdWithUserAndComments(id)
                .orElseThrow(() -> new Exception404("해당 질문을 찾을 수 없습니다."));
        qna.increaseHits(); // 조회수 증가
        return new QnaResponse.DetailDTO(qna, sessionUser);
    }

    // 질문 등록
    @Transactional
    public void save(QnaRequest.SaveDTO requestDTO, UserResponse.LoginDTO sessionUser) {
        User user = User.builder().id(sessionUser.getId()).build();
        qnaRepository.save(requestDTO.toEntity(user));
    }

    // 질문 수정
    @Transactional
    public void update(Long id, QnaRequest.UpdateDTO requestDTO, UserResponse.LoginDTO sessionUser) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 질문을 찾을 수 없습니다."));

        // 권한 체크
        if (!qna.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403("수정 권한이 없습니다.");
        }

        // 답변 여부 체크 (비즈니스 룰: 답변 완료 시 수정 불가)
        if (qna.getIsAnswered()) {
            throw new Exception400("답변이 완료된 질문은 수정할 수 없습니다.");
        }

        qna.update(requestDTO.getTitle(), requestDTO.getContent(), requestDTO.getCategory());
    }

    // 질문 삭제
    @Transactional
    public void delete(Long id, UserResponse.LoginDTO sessionUser) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 질문을 찾을 수 없습니다."));

        // 권한 체크 (작성자 본인 또는 관리자만 삭제 가능)
        boolean isOwner = qna.getUser().getId().equals(sessionUser.getId());
        boolean isAdmin = sessionUser.isAdmin();

        if (!isOwner && !isAdmin) {
            throw new Exception403("삭제 권한이 없습니다.");
        }

        qnaRepository.delete(qna);
    }

    // 답변(댓글) 등록 - 관리자 전용
    @Transactional
    public void saveComment(Long qnaId, String content, UserResponse.LoginDTO sessionAdmin) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new Exception404("해당 질문을 찾을 수 없습니다."));

        User admin = User.builder().id(sessionAdmin.getId()).build();
        Comment comment = Comment.builder()
                .qna(qna)
                .admin(admin)
                .content(content)
                .build();

        commentRepository.save(comment);
        qna.answered(); // 질문 상태를 '답변완료'로 변경
    }
}

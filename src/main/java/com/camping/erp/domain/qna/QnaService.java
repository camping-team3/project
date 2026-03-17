package com.camping.erp.domain.qna;

import com.camping.erp.domain.user.User;
import com.camping.erp.global.handler.ex.Exception400;
import com.camping.erp.global.handler.ex.Exception403;
import com.camping.erp.global.handler.ex.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {
    private final QnaRepository qnaRepository;
    private final CommentRepository commentRepository;

    // 전체 목록 조회 (필터링 포함)
    public List<QnaResponse.ListDTO> findAll(String status, User sessionUser) {
        List<Qna> qnas;
        if ("pending".equalsIgnoreCase(status)) {
            qnas = qnaRepository.findByIsAnsweredWithUser(false);
        } else if ("completed".equalsIgnoreCase(status)) {
            qnas = qnaRepository.findByIsAnsweredWithUser(true);
        } else {
            qnas = qnaRepository.findAllWithUser();
        }
        
        return qnas.stream()
                .map(qna -> new QnaResponse.ListDTO(qna, sessionUser))
                .toList();
    }

    // 상세 조회
    @Transactional
    public QnaResponse.DetailDTO findById(Long id, User sessionUser) {
        Qna qna = qnaRepository.findByIdWithUserAndComments(id)
                .orElseThrow(() -> new Exception404("해당 질문을 찾을 수 없습니다."));
        qna.increaseHits(); // 조회수 증가
        return new QnaResponse.DetailDTO(qna, sessionUser);
    }

    // 질문 등록
    @Transactional
    public void save(QnaRequest.SaveDTO requestDTO, User sessionUser) {
        qnaRepository.save(requestDTO.toEntity(sessionUser));
    }

    // 질문 수정
    @Transactional
    public void update(Long id, QnaRequest.UpdateDTO requestDTO, User sessionUser) {
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
    public void delete(Long id, User sessionUser) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 질문을 찾을 수 없습니다."));

        // 권한 체크 (작성자 또는 관리자만 삭제 가능 - 여기서는 일단 작성자만)
        if (!qna.getUser().getId().equals(sessionUser.getId())) {
            throw new Exception403("삭제 권한이 없습니다.");
        }

        qnaRepository.delete(qna);
    }

    // 답변(댓글) 등록 - 관리자 전용
    @Transactional
    public void saveComment(Long qnaId, String content, User sessionAdmin) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new Exception404("해당 질문을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .qna(qna)
                .admin(sessionAdmin)
                .content(content)
                .build();

        commentRepository.save(comment);
        qna.answered(); // 질문 상태를 '답변완료'로 변경
    }
}

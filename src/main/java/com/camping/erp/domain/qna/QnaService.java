package com.camping.erp.domain.qna;

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

    public List<Qna> findAll() {
        return qnaRepository.findAll();
    }
}

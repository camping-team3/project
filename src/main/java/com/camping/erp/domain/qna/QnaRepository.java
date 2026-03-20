package com.camping.erp.domain.qna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    
    @Query("select q from Qna q join fetch q.user")
    Page<Qna> findAllWithUser(Pageable pageable);

    @Query("select q from Qna q join fetch q.user where q.isAnswered = :isAnswered")
    Page<Qna> findByIsAnsweredWithUser(Boolean isAnswered, Pageable pageable);

    @Query("select q from Qna q join fetch q.user left join fetch q.comments c left join fetch c.admin where q.id = :id")
    Optional<Qna> findByIdWithUserAndComments(Long id);

    @Query("select count(q) from Qna q where q.isAnswered = false")
    Long countUnanswered();
    }

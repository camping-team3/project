package com.camping.erp.domain.qna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    
    @Query("select q from Qna q join fetch q.user order by q.createdAt desc")
    List<Qna> findAllWithUser();

    @Query("select q from Qna q join fetch q.user where q.isAnswered = :isAnswered order by q.createdAt desc")
    List<Qna> findByIsAnsweredWithUser(Boolean isAnswered);

    @Query("select q from Qna q join fetch q.user left join fetch q.comments c left join fetch c.admin where q.id = :id")
    Optional<Qna> findByIdWithUserAndComments(Long id);
}

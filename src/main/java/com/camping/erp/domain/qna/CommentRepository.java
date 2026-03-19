package com.camping.erp.domain.qna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select count(c) from Comment c where c.createdAt >= :startOfDay")
    Long countTodayComments(@Param("startOfDay") LocalDateTime startOfDay);
}

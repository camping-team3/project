package com.camping.erp.domain.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n " +
           "WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword% " +
           "ORDER BY n.isTop DESC, n.id DESC")
    Page<Notice> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT n FROM Notice n ORDER BY n.isTop DESC, n.id DESC")
    Page<Notice> findAllOrderByIsTopDescAndIdDesc(Pageable pageable);
}

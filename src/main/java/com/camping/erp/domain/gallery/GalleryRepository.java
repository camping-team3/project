package com.camping.erp.domain.gallery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    @Query("SELECT g FROM Gallery g ORDER BY g.id DESC")
    Page<Gallery> findAllOrderByIdDesc(Pageable pageable);

    @Query("SELECT g FROM Gallery g WHERE g.title LIKE %:keyword% OR g.content LIKE %:keyword% ORDER BY g.id DESC")
    Page<Gallery> findByKeywordOrderByIdDesc(String keyword, Pageable pageable);
}

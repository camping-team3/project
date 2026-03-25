package com.camping.erp.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE image_tb SET file_path = '/images/', " +
                   "file_name = CASE " +
                   "  WHEN MOD(id, 10) = 1 THEN '1.png' " +
                   "  WHEN MOD(id, 10) = 2 THEN '2.png' " +
                   "  WHEN MOD(id, 10) = 3 THEN '3.jpg' " +
                   "  WHEN MOD(id, 10) = 4 THEN '4.jpg' " +
                   "  WHEN MOD(id, 10) = 5 THEN '5.jpg' " +
                   "  WHEN MOD(id, 10) = 6 THEN '6.jpg' " +
                   "  WHEN MOD(id, 10) = 7 THEN 'camping_review1.jpg' " +
                   "  WHEN MOD(id, 10) = 8 THEN 'camping_review2.jpg' " +
                   "  WHEN MOD(id, 10) = 9 THEN 'camping_review3.jpg' " +
                   "  ELSE 'Camping_map.png' " +
                   "END", nativeQuery = true)
    void migrateAllToStaticImages();
}

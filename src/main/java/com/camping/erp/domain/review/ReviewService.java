package com.camping.erp.domain.review;

import com.camping.erp.domain.image.Image;
import com.camping.erp.domain.image.ImageRepository;
import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.reservation.ReservationRepository;
import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.review.dto.ReviewRequest;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.Zone;
import com.camping.erp.domain.user.User;
import com.camping.erp.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ImageRepository imageRepository;
    private final FileUtil fileUtil;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    public void save(User user, ReviewRequest.SaveDTO req) {
        // 1. 예약 검증
        Reservation reservation = reservationRepository.findById(req.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 예약만 리뷰를 작성할 수 있습니다.");
        }
        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new IllegalArgumentException("이용 완료된 예약만 리뷰를 작성할 수 있습니다.");
        }

        // 2. 중복 검증
        if (reviewRepository.existsByReservationId(reservation.getId())) {
            throw new IllegalArgumentException("이미 리뷰를 작성한 예약입니다.");
        }

        // 3. 엔티티 생성 및 저장
        Review review = Review.builder()
                .user(user)
                .reservation(reservation)
                .rating(req.getRating())
                .content(req.getContent())
                .build();
        reviewRepository.save(review);

        // 4. Site & Zone 평점 반영
        Site site = reservation.getSite();
        Zone zone = site.getZone();
        site.addRating(req.getRating());
        zone.addRating(req.getRating());

        // 5. 이미지 저장 (최대 5장)
        if (req.getImages() != null && !req.getImages().isEmpty()) {
            if (req.getImages().size() > 5) {
                throw new IllegalArgumentException("이미지는 최대 5장까지 업로드 가능합니다.");
            }
            for (MultipartFile file : req.getImages()) {
                if (file.isEmpty()) continue;
                String fileName = fileUtil.uploadFile(file);
                Image image = Image.builder()
                        .review(review)
                        .filePath("/upload/" + fileName)
                        .fileName(fileName)
                        .build();
                imageRepository.save(image);
            }
        }
    }

    @Transactional
    public void deleteByAdmin(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        // 1. 평점 역산
        Reservation reservation = review.getReservation();
        Site site = reservation.getSite();
        Zone zone = site.getZone();
        site.removeRating(review.getRating());
        zone.removeRating(review.getRating());

        // 2. 이미지 물리적 삭제 및 DB 삭제
        for (Image image : review.getImages()) {
            fileUtil.deleteFile(image.getFileName());
            imageRepository.delete(image);
        }

        // 3. 리뷰 삭제
        reviewRepository.delete(review);
    }
}

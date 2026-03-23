package com.camping.erp.domain.review;

import com.camping.erp.domain.admin.AdminResponse;
import com.camping.erp.domain.image.Image;
import com.camping.erp.domain.image.ImageRepository;
import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.reservation.ReservationRepository;
import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.review.dto.ReviewRequest;
import com.camping.erp.domain.review.dto.ReviewResponse;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.site.Zone;
import com.camping.erp.domain.user.User;
import com.camping.erp.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ImageRepository imageRepository;
    private final FileUtil fileUtil;
    private final AiAnalysisService aiAnalysisService;

    // 관리자용 리뷰 목록 조회 (페이징)
    public AdminResponse.ReviewPageDTO findAllForAdmin(Pageable pageable) {
        Page<Review> page = reviewRepository.findAllWithDetails(pageable);

        List<AdminResponse.ReviewListDTO> dtoList = page.getContent().stream()
                .map(r -> AdminResponse.ReviewListDTO.builder()
                        .id(r.getId())
                        .username(r.getUser().getUsername())
                        .rating(r.getRating())
                        .content(r.getContent())
                        .zoneName(r.getReservation().getSite().getZone().getName())
                        .siteName(r.getReservation().getSite().getSiteName())
                        .createdAt(r.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .images(r.getImages().stream().map(Image::getFileName).collect(Collectors.toList()))
                        .aiDangerScore(r.getAiDangerScore())
                        .isReviewed(r.isReviewed())
                        .isDeleted(r.isDeleted())
                        .adminReason(r.getAdminReason())
                        .build())
                .toList();

        int totalPages = page.getTotalPages();
        int currentPage = page.getNumber();
        int startPage = Math.max(0, (currentPage / 5) * 5);
        int endPage = Math.min(startPage + 4, totalPages - 1);

        List<AdminResponse.PageNumberDTO> pageNumbers = IntStream.rangeClosed(startPage, endPage)
                .mapToObj(n -> AdminResponse.PageNumberDTO.builder()
                        .number(n)
                        .displayDigit(n + 1)
                        .isCurrent(n == currentPage)
                        .build())
                .toList();

        AdminResponse.PaginationDTO pagination = AdminResponse.PaginationDTO.builder()
                .totalPages(totalPages)
                .totalElements(page.getTotalElements())
                .currentPage(currentPage)
                .pageNumbers(pageNumbers)
                .hasPrev(page.hasPrevious())
                .hasNext(page.hasNext())
                .prevPage(currentPage - 1)
                .nextPage(currentPage + 1)
                .build();

        return AdminResponse.ReviewPageDTO.builder()
                .reviews(dtoList)
                .pagination(pagination)
                .build();
    }

    public ReviewResponse.ListWrapperDTO findAll(Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAllWithDetails(pageable);
        Double avgRating = reviewRepository.findAverageRating();
        long totalCount = reviewRepository.count();

        List<ReviewResponse.ListDTO> reviews = reviewPage.getContent().stream()
                .map(ReviewResponse.ListDTO::new)
                .collect(Collectors.toList());

        List<ReviewResponse.PageNumberDTO> pages = IntStream.range(0, reviewPage.getTotalPages())
                .boxed()
                .map(n -> new ReviewResponse.PageNumberDTO(n, reviewPage.getNumber()))
                .collect(Collectors.toList());

        return ReviewResponse.ListWrapperDTO.builder()
                .reviews(reviews)
                .avgRating(avgRating != null ? Math.round(avgRating * 10) / 10.0 : 0.0)
                .totalCount(totalCount)
                .hasPrev(reviewPage.hasPrevious())
                .hasNext(reviewPage.hasNext())
                .pages(pages)
                .prevPage(reviewPage.getNumber() - 1)
                .nextPage(reviewPage.getNumber() + 1)
                .build();
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // 마이페이지용 내 리뷰 조회
    public List<ReviewResponse.ListDTO> findByUserId(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ReviewResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
    }

    @Transactional
    public void update(Long reviewId, User user, ReviewRequest.UpdateDTO req) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 리뷰만 수정할 수 있습니다.");
        }

        // 평점 업데이트 (차이만큼 가감)
        Site site = review.getReservation().getSite();
        Zone zone = site.getZone();
        
        // 기존 점수 제거 후 새 점수 추가
        site.removeRating(review.getRating());
        zone.removeRating(review.getRating());
        site.addRating(req.getRating());
        zone.addRating(req.getRating());

        // 내용 수정
        review.update(req.getRating(), req.getContent());

        // 이미지 업데이트 (이미지가 있는 경우 기존 것 삭제 후 새로 등록)
        if (req.getImages() != null && !req.getImages().isEmpty() && !req.getImages().get(0).isEmpty()) {
            // 기존 이미지 물리적 삭제
            for (Image image : review.getImages()) {
                fileUtil.deleteFile(image.getFileName());
                imageRepository.delete(image);
            }
            review.getImages().clear();

            // 새 이미지 저장
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
    public void deleteByUser(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        // 평점 차감
        Site site = review.getReservation().getSite();
        Zone zone = site.getZone();
        site.removeRating(review.getRating());
        zone.removeRating(review.getRating());

        // 이미지 물리적 삭제
        for (Image image : review.getImages()) {
            fileUtil.deleteFile(image.getFileName());
            imageRepository.delete(image);
        }

        reviewRepository.delete(review);
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

        // 6. AI 비방 분석 (비동기 호출)
        aiAnalysisService.analyzeReviewAsync(review.getId(), review.getContent());
    }

    @Transactional
    public void deleteByAdmin(Long reviewId, String reason) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        // 1. 평점 역산 (실시간 반영)
        Reservation reservation = review.getReservation();
        Site site = reservation.getSite();
        Zone zone = site.getZone();
        site.removeRating(review.getRating());
        zone.removeRating(review.getRating());

        // 2. 논리 삭제 처리 (이미지는 물리적으로 삭제하지 않고 보존)
        review.processByAdmin(true, reason);
    }

    @Transactional
    public void keepByAdmin(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));

        // 검토 완료 상태로만 변경 (isDeleted = false)
        review.processByAdmin(false, null);
    }
}

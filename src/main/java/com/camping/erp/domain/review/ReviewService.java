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
import com.camping.erp.domain.site.SiteRepository;
import com.camping.erp.domain.site.Zone;
import com.camping.erp.domain.site.ZoneRepository;
import com.camping.erp.domain.user.User;
import com.camping.erp.domain.user.enums.UserStatus;
import com.camping.erp.global.util.FileUtil;
import com.camping.erp.global.handler.ex.Exception400;
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
    private final SiteRepository siteRepository;
    private final ZoneRepository zoneRepository;
    private final FileUtil fileUtil;

    /**
     * [관리자용] 리뷰 목록 조회 (페이징, 필터, 검색 적용)
     * @param isPending true면 '검토 대기 중'만 필터링합니다.
     * @param keyword 검색어 (작성자/내용)
     * @param pageable 페이징 정보 (기존 페이징 유지)
     */
    public AdminResponse.ReviewPageDTO findAllForAdmin(boolean isPending, String keyword, Pageable pageable) {
        // [수정] 페이징(Pageable)을 유지하면서 필터링과 검색이 가능한 리포지토리 메서드를 호출합니다.
        Page<Review> page = reviewRepository.findAllForAdmin(isPending, keyword, pageable);

        List<AdminResponse.ReviewListDTO> dtoList = page.getContent().stream()
                .map(r -> AdminResponse.ReviewListDTO.builder()
                        .id(r.getId())
                        .username(r.getUser().getUsername())
                        .rating(r.getRating())
                        .content(r.getContent())
                        .zoneName(r.getReservation().getSite().getZone().getName())
                        .siteName(r.getReservation().getSite().getSiteName())
                        .createdAt(r.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        // [수정] 파일명만 보내는 게 아니라 전체 경로(filePath + fileName)를 보내 이미지 깨짐을 방지합니다.
                        .images(r.getImages().stream().map(img -> img.getFilePath() + img.getFileName()).collect(Collectors.toList()))
                        .aiDangerScore(r.getAiDangerScore())
                        .isReviewed(r.isReviewed())
                        .isDeleted(r.isDeleted())
                        .adminReason(r.getAdminReason())
                        .penaltyCount(r.getUser().getPenaltyCount())
                        .userId(r.getUser().getId())
                        .isExpelled(r.getUser().getStatus() == UserStatus.ANONYMOUS)
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
        // [수정] 삭제된 리뷰까지 세던 기존 count() 대신, 실제 서비스 중인 리뷰만 세는 메서드를 사용합니다.
        long totalCount = reviewRepository.countByIsDeletedFalse();

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

    public List<ReviewResponse.ListDTO> findByUserId(Long userId) {
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ReviewResponse.ListDTO::new)
                .collect(Collectors.toList());
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new Exception400("존재하지 않는 리뷰입니다."));
    }

    @Transactional
    public Review update(Long reviewId, User user, ReviewRequest.UpdateDTO req) {
        if (com.camping.erp.global.util.ProfanityFilter.containsProfanity(req.getContent())) {
            throw new Exception400("비속어가 포함된 내용은 수정할 수 없습니다.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception400("존재하지 않는 리뷰입니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new Exception400("본인의 리뷰만 수정할 수 있습니다.");
        }

        Site site = review.getReservation().getSite();
        Zone zone = site.getZone();
        site.removeRating(review.getRating());
        zone.removeRating(review.getRating());
        site.addRating(req.getRating());
        zone.addRating(req.getRating());
        review.update(req.getRating(), req.getContent());

        if (req.getImages() != null && !req.getImages().isEmpty() && !req.getImages().get(0).isEmpty()) {
            for (Image image : review.getImages()) {
                fileUtil.deleteFile(image.getFileName());
                imageRepository.delete(image);
            }
            review.getImages().clear();
            for (MultipartFile file : req.getImages()) {
                if (file.isEmpty()) continue;
                String fileName = fileUtil.uploadFile(file);
                Image image = Image.builder().review(review).filePath("/upload/" + fileName).fileName(fileName).build();
                imageRepository.save(image);
            }
        }
        return review;
    }

    @Transactional
    public void deleteByUser(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception400("존재하지 않는 리뷰입니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new Exception400("본인의 리뷰만 삭제할 수 있습니다.");
        }

        Site site = review.getReservation().getSite();
        Zone zone = site.getZone();
        site.removeRating(review.getRating());
        zone.removeRating(review.getRating());

        for (Image image : review.getImages()) {
            fileUtil.deleteFile(image.getFileName());
            imageRepository.delete(image);
        }
        reviewRepository.delete(review);
    }

    @Transactional
    public Review save(User user, ReviewRequest.SaveDTO req) {
        if (com.camping.erp.global.util.ProfanityFilter.containsProfanity(req.getContent())) {
            throw new Exception400("비속어가 포함된 내용은 등록할 수 없습니다.");
        }

        Reservation reservation = reservationRepository.findById(req.getReservationId())
                .orElseThrow(() -> new Exception400("존재하지 않는 예약입니다."));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new Exception400("본인의 예약만 리뷰를 작성할 수 있습니다.");
        }
        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new Exception400("이용 완료된 예약만 리뷰를 작성할 수 있습니다.");
        }
        if (reviewRepository.existsByReservationId(reservation.getId())) {
            throw new Exception400("이미 리뷰를 작성한 예약입니다.");
        }

        Review review = Review.builder()
                .user(user)
                .reservation(reservation)
                .rating(req.getRating())
                .content(req.getContent())
                .build();
        reviewRepository.save(review);

        recalculateAverageRating(reservation.getSite().getId(), reservation.getSite().getZone().getId());

        if (req.getImages() != null && !req.getImages().isEmpty()) {
            if (req.getImages().size() > 5) {
                throw new Exception400("이미지는 최대 5장까지 업로드 가능합니다.");
            }
            for (MultipartFile file : req.getImages()) {
                if (file.isEmpty()) continue;
                String fileName = fileUtil.uploadFile(file);
                Image image = Image.builder().review(review).filePath("/upload/" + fileName).fileName(fileName).build();
                imageRepository.save(image);
            }
        }
        return review;
    }

    @Transactional
    public void deleteByAdmin(Long reviewId, String reason) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception400("존재하지 않는 리뷰입니다."));
        review.processByAdmin(true, reason);
        review.getUser().increasePenalty();
        recalculateAverageRating(review.getReservation().getSite().getId(), review.getReservation().getSite().getZone().getId());
    }

    /**
     * [관리자용] 리뷰 유지 처리
     * 리뷰에 문제가 없다고 판단되면 '검토 완료' 상태로 변경하여 '검토 대기중' 목록에서 제외합니다.
     */
    @Transactional
    public void keepByAdmin(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception400("존재하지 않는 리뷰입니다."));
        
        // [핵심] isReviewed를 true로 바꿔서 '검토 완료'임을 표시합니다.
        // 첫 번째 인자 false는 삭제하지 않겠다는 의미(유지)입니다.
        review.processByAdmin(false, null); 
    }
    public void recalculateAverageRating(Long siteId, Long zoneId) {
        List<Review> siteReviews = reviewRepository.findActiveReviewsBySiteId(siteId);
        double siteAvg = siteReviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        siteRepository.findById(siteId).ifPresent(site -> site.updateRating(siteReviews.size(), siteAvg));

        List<Review> zoneReviews = reviewRepository.findActiveReviewsByZoneId(zoneId);
        double zoneAvg = zoneReviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        zoneRepository.findById(zoneId).ifPresent(zone -> zone.updateRating(zoneReviews.size(), zoneAvg));
    }
}

package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.RequestStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

/**
 * 예약 변경 요청 엔티티
 * 고객이 요청한 변경 사항(날짜, 사이트, 인원 등)을 관리자 승인 전까지 보관합니다.
 */
@Entity
@Getter
@Table(name = "reservation_change_request")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class ReservationChangeRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    // 변경하고자 하는 새로운 정보들
    @Column(nullable = false)
    private LocalDate newCheckIn;

    @Column(nullable = false)
    private LocalDate newCheckOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_site_id", nullable = false)
    private Site newSite;

    @Column(nullable = false)
    private Integer newPeopleCount;

    private String newVisitorName;

    private String newVisitorPhone;

    // 요청 처리 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    // 관리자 거절 사유
    private String rejectionReason;

    // --- 비즈니스 로직 (캡슐화) ---

    /**
     * 관리자 승인 처리
     */
    public void approve() {
        this.status = RequestStatus.APPROVED;
        this.rejectionReason = null; // 승인 시 기존 거절 사유 초기화
    }

    /**
     * 관리자 거절 처리
     * @param reason 거절 사유
     */
    public void reject(String reason) {
        this.status = RequestStatus.REJECTED;
        this.rejectionReason = reason;
    }
}

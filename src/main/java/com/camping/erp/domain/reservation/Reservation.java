package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.ReservationStatus;
import com.camping.erp.domain.site.Site;
import com.camping.erp.domain.user.User;
import com.camping.erp.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservation_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    private Integer peopleCount; // 예약 인원

    private String visitorName; // 방문자 성함
    private String visitorPhone; // 방문자 연락처

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // PENDING, CONFIRMED, CANCEL_REQ, CANCEL_COMP, COMPLETED

    // --- 양방향 연관관계 추가 (이력 관리용) ---

    // mappedBy: 연관관계의 주인이 ReservationChangeRequest의 'reservation' 필드임을 명시
    // cascade = CascadeType.ALL: 부모(Reservation)의 저장/삭제가 자식에게도 전파됨
    // orphanRemoval = true: 리스트에서 제거된 자식 객체를 DB에서도 자동으로 삭제함
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationChangeRequest> changeRequests = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationCancelRequest> cancelRequests = new ArrayList<>();

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private com.camping.erp.domain.payment.Payment payment; // 결제 정보 참조

    @Builder
    public Reservation(Long id, User user, Site site, LocalDate checkIn, LocalDate checkOut, Long totalPrice,
            Integer peopleCount, String visitorName, String visitorPhone, ReservationStatus status) {
        this.id = id;
        this.user = user;
        this.site = site;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.peopleCount = peopleCount;
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
        this.status = status;
    }

    // --- 비즈니스 로직 추가 ---

    /**
     * 예약 상태 변경
     */
    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }

    /**
     * 예약 정보 업데이트 (변경 승인 시 사용)
     */
    public void updateReservationInfo(LocalDate checkIn, LocalDate checkOut, Site site, Integer peopleCount,
            String visitorName, String visitorPhone) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.site = site;
        this.peopleCount = peopleCount;
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
    }
}

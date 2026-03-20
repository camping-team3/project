package com.camping.erp.domain.reservation;

import com.camping.erp.domain.reservation.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 예약 변경 요청 전용 Repository
 */
public interface ReservationChangeRequestRepository extends JpaRepository<ReservationChangeRequest, Long> {
    /**
     * 특정 상태(PENDING 등)의 모든 변경 요청 목록 조회 (관리자용)
     */
    List<ReservationChangeRequest> findByStatus(RequestStatus status);

    /**
     * 특정 예약 건에 속한 모든 변경 요청 이력 조회
     */
    List<ReservationChangeRequest> findByReservationId(Long reservationId);
}

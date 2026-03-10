package com.camping.erp.domain.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final ReservationRepository reservationRepository;

    // TODO: 예약 생성 로직 (Redis 분산 락 적용 필요)
    @Transactional
    public void createReservation() {
        // 예약 생성 비즈니스 로직
    }
}

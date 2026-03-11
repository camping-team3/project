package com.camping.erp.domain.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request) {
        // 직접 구현하세요.
        return null;
    }

    @Transactional
    public void cancel(Long id) {
        // 직접 구현하세요.
    }
}

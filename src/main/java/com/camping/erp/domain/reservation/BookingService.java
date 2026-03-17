package com.camping.erp.domain.reservation;

import com.camping.erp.domain.site.SeasonRepository;
import com.camping.erp.domain.site.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final ReservationRepository reservationRepository;
    private final SeasonRepository seasonRepository;

    /**
     * 예약 총 금액 계산
     * @param zone 구역 정보 (기본 요금, 성수기 요금, 기준 인원, 추가 요금 포함)
     * @param peopleCount 예약 인원
     * @param checkIn 체크인 날짜
     * @param checkOut 체크아웃 날짜
     * @return 총 결제 금액
     */
    public Long calculateTotalPrice(Zone zone, int peopleCount, LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || checkOut.isBefore(checkIn) || checkIn.equals(checkOut)) {
            throw new IllegalArgumentException("올바르지 않은 예약 기간입니다.");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        // 1. 기간 중 성수기 포함 여부 확인 (하루라도 포함되면 전체 성수기 요금 적용 또는 날짜별 계산 중 선택 가능)
        // 여기서는 정책상 하루라도 포함되면 전체 성수기 요금을 적용하는 방식으로 우선 구현 (비즈니스 룰에 따라 변경 가능)
        boolean isPeak = seasonRepository.existsByDateRange(checkIn, checkOut);
        Long dailyPrice = isPeak ? zone.getPeakPrice() : zone.getNormalPrice();

        // 2. 기본 숙박 요금 계산
        Long baseStayFee = dailyPrice * nights;

        // 3. 인원 추가 요금 계산
        int extraPeople = Math.max(0, peopleCount - zone.getBasePeople());
        Long extraFeePerNight = extraPeople * zone.getExtraPersonFee();
        Long totalExtraFee = extraFeePerNight * nights;

        return baseStayFee + totalExtraFee;
    }

    @Transactional
    public ReservationResponse.ReserveDTO reserve(ReservationRequest.ReserveDTO request) {
        // 예약 생성 로직 (엔티티 변환 및 저장) - 차후 구현
        return null;
    }

    @Transactional
    public void cancel(Long id) {
        // 예약 취소 로직 - 차후 구현
    }
}

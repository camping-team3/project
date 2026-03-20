package com.camping.erp.global.scheduler;

import com.camping.erp.domain.reservation.Reservation;
import com.camping.erp.domain.reservation.ReservationRepository;
import com.camping.erp.domain.reservation.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 예약 상태 자동 업데이트 스케줄러
 * 매일 자정 및 서버 기동 시 체크아웃 날짜가 지난 예약 건들을 찾아 '이용 완료(COMPLETED)' 상태로 변경합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;

    /**
     * 서버 기동 완료 시 즉시 실행 (실습 환경 대응)
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onStartUpdate() {
        log.info("--- 서버 기동: 예약 상태 체크 시작 ---");
        autoUpdateReservationStatus();
        log.info("--- 서버 기동: 예약 상태 체크 종료 ---");
    }

    /**
     * 매일 자정(00:00:00)에 실행
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void autoUpdateReservationStatus() {
        LocalDate today = LocalDate.now();
        
        // 1. 체크아웃 날짜가 오늘보다 이전(과거)이고, 현재 '확정(CONFIRMED)' 상태인 예약 목록 조회
        List<Reservation> expiredReservations = reservationRepository.findByStatusAndCheckOutBefore(
                ReservationStatus.CONFIRMED, today
        );

        if (expiredReservations.isEmpty()) {
            log.info("자동 업데이트할 예약 건이 없습니다.");
            return;
        }

        // 2. 조회된 예약 건들의 상태를 '이용 완료(COMPLETED)'로 일괄 변경
        for (Reservation reservation : expiredReservations) {
            reservation.updateStatus(ReservationStatus.COMPLETED);
            log.info("예약 ID: {} 상태 변경 완료 (CONFIRMED -> COMPLETED)", reservation.getId());
        }
        
        log.info("총 {} 건의 예약 상태가 자동으로 업데이트되었습니다.", expiredReservations.size());
    }
}

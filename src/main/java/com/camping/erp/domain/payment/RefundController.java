package com.camping.erp.domain.payment;

import com.camping.erp.global.util.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 고객이 마이페이지에서 직접 환불을 트리거하기 위한 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refund")
public class RefundController {

    private final RefundService refundService;

    /**
     * 예약 변경(박수 감소)에 따른 차액 부분 환불 실행
     */
    @PostMapping(value = "/execute/change/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> executeChangeRefund(@PathVariable("requestId") Long requestId) {
        refundService.processPartialRefund(requestId);
        return Resp.ok("차액 환불 처리가 완료되었습니다.");
    }

    /**
     * 예약 취소 승인 건에 대한 환불 실행
     */
    @PostMapping(value = "/execute/cancel/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> executeCancelRefund(@PathVariable("requestId") Long requestId) {
        refundService.processCancelRefund(requestId);
        return Resp.ok("취소 환불 처리가 완료되었습니다.");
    }
}

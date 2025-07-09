package com.talkjang.demo.common.enums;

public enum OrderState {
    PENDING_PAYMENT,       // 결제 대기 중
    PAID,                  // 결제 완료
    READY_FOR_SHIPMENT,    // 운송장 등록 완료
    COMPLETED,             // 구매확정 (거래 완료)
    CANCELLED,              // 주문 취소 완료
    DIRECT
}

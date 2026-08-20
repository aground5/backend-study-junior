package com.gdgku.study.backend.common.annotation;

/**
 * 검증/스테이징 전용 결제 구현체 (토스페이)
 */
@MyProfile("staging")
public class TossPayPaymentService implements PaymentService {
    @Override
    public String pay(int amount) {
        String msg = "[STAGING TEST] 토스페이 테스트 환경 승인 완료 ($" + amount + ")";
        System.out.println(msg);
        return msg;
    }
}

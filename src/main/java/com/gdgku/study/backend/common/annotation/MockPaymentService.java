package com.gdgku.study.backend.common.annotation;

/**
 * 로컬 개발/디버깅 전용 가짜 결제 구현체
 */
@MyProfile("local")
public class MockPaymentService implements PaymentService {
    @Override
    public String pay(int amount) {
        String msg = "[LOCAL MOCK] PG사 결제 연동 생략 - 가짜 승인 완료 ($" + amount + ")";
        System.out.println(msg);
        return msg;
    }
}

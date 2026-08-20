package com.gdgku.study.backend.common.annotation;

/**
 * 운영/배포 전용 실제 결제 구현체 (카카오페이)
 */
@MyProfile("prod")
public class KakaoPayPaymentService implements PaymentService {
    @Override
    public String pay(int amount) {
        String msg = "[PROD RELEASE] 카카오페이 실서비스 PG API 결제 요청 완료 ($" + amount + ")";
        System.out.println(msg);
        return msg;
    }
}

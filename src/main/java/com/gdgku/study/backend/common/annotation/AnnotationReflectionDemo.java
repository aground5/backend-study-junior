package com.gdgku.study.backend.common.annotation;

import java.util.List;

/**
 * Java Reflection API로 여러 구현 클래스의 @MyProfile 메타데이터를 스캔하고,
 * 활성화된 환경 프로필에 맞는 구현체를 동적으로 바인딩하는 데모입니다.
 */
public class AnnotationReflectionDemo {

    // 탐색 대상 결제 서비스 구현 클래스 후보군
    private static final List<Class<? extends PaymentService>> CANDIDATE_CLASSES = List.of(
            MockPaymentService.class,
            KakaoPayPaymentService.class,
            TossPayPaymentService.class
    );

    public static void main(String[] args) {
        System.out.println("[프로필별 결제 서비스 동적 바인딩 테스트]\n");

        resolveAndPay("local", 10000);
        resolveAndPay("prod", 50000);
        resolveAndPay("staging", 25000);
        resolveAndPay("unknown", 1000);
    }

    public static PaymentService resolveAndPay(String activeProfile, int amount) {
        System.out.println("============================================================");
        System.out.println("활성화 환경 (activeProfile): [" + activeProfile + "]");
        System.out.println("후보 구현체 수: " + CANDIDATE_CLASSES.size() + "개");

        PaymentService matchedService = null;

        for (Class<? extends PaymentService> clazz : CANDIDATE_CLASSES) {
            // 1. 클래스에 @MyProfile 어노테이션이 부착되어 있는지 확인
            if (clazz.isAnnotationPresent(MyProfile.class)) {
                MyProfile profileAnnotation = clazz.getAnnotation(MyProfile.class);
                String targetProfile = profileAnnotation.value();

                // 2. 활성화된 프로필과 어노테이션 메타데이터 비교
                if (activeProfile.equals(targetProfile)) {
                    try {
                        // 3. 리플렉션으로 객체를 동적 인스턴스화 (Spring IoC의 빈 생성 원리)
                        matchedService = clazz.getDeclaredConstructor().newInstance();
                        System.out.println("-> [매칭 성공] " + clazz.getSimpleName() + " 빈(Bean) 선택됨");
                        break;
                    } catch (Exception e) {
                        System.err.println("-> [오류] 객체 생성 실패: " + e.getMessage());
                    }
                }
            }
        }

        if (matchedService != null) {
            matchedService.pay(amount);
        } else {
            System.out.println("-> [경고] 해당 프로필(" + activeProfile + ")에 매칭되는 PaymentService 구현체를 찾을 수 없습니다.");
        }
        System.out.println("============================================================\n");
        return matchedService;
    }
}

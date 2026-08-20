package com.gdgku.study.backend.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 커스텀 프로필 어노테이션
 * 
 * - @Retention(RetentionPolicy.RUNTIME): 런타임 시점까지 메타데이터를 유지하여 Reflection API로 조회 가능합니다.
 * - @Target(ElementType.TYPE): 클래스 및 인터페이스 상단에 선언합니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyProfile {
    String value();
}

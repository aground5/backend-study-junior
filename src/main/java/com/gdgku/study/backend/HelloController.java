package com.gdgku.study.backend; // 메인 클래스와 동일 패키지 (또는 하위 패키지)

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring!";
    }
}
package com.gdgku.study.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "안녕하세요? 니콜라스 케이지입니다.";
    }
    @GetMapping("/bye")
    public String bye() {
        return "잘가슈";
    }
}
// requestcontroller
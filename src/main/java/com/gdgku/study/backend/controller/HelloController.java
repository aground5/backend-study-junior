package com.gdgku.study.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("springHelloController")
public class HelloController {

    @GetMapping("/hello-spring")
    public String hello() {
        return "Hello, Spring!";
    }
}
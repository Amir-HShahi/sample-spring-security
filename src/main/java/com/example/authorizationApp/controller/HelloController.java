package com.example.authorizationApp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    String getSession(HttpServletRequest request) {
        return "Hello: SessionID: " + request.getSession().getId();
    }
}

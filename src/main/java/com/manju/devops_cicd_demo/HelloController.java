package com.manju.devops_cicd_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "DevOps CI/CD Demo - Version 2.0";
    }
}
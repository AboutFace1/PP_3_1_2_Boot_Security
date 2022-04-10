package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {

    @RequestMapping(value = {"/login", ""})
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/logout")
    public String getLoginPageAfterLogout() {
        return "login";
    }

}

package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class AuthController {

    @RequestMapping(value = {"/login", ""})
    public String getLoginPage(Principal principal) {
        return "login";
    }

    @PostMapping("/logout")
    public String getLoginPageAfterLogout() {
        return "login";
    }

}

package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
public class MainPageController {

    UserService userService;

    @Autowired
    public MainPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String defaultPage() {
        return "redirect:/auth/login";
    }

    @RequestMapping({"/user", "/admin"})
    public String user(Principal principal, Model model) {

        User user = userService.getUserByName(principal.getName());

        model.addAttribute("user", user);

        return "user";
    }
}

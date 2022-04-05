package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    PasswordEncoder passwordEncoder;
    UserService userService;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String defaultPage(Model model, Principal principal) {

        User user = userService.getUserByName(principal.getName());

        model.addAttribute("people", userService.getAll());
        model.addAttribute("user", user);

        return "admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") int id, @RequestParam(value = "role", required = false) Role role) {
        if (bindingResult.hasErrors()) {
            return "admin";
        }

        if (role != null) user.setRole(role);

        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }


    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, // Default constr from modelAttribute sets default Role and Status
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin";

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin";
    }
}

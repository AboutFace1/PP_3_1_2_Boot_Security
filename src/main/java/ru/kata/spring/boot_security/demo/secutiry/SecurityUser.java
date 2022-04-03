package ru.kata.spring.boot_security.demo.secutiry;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.Status;
import ru.kata.spring.boot_security.demo.model.User;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SecurityUser extends org.springframework.security.core.userdetails.User {

    private String name;

    public SecurityUser(String name, String username, String password, Set<SimpleGrantedAuthority> authorities, boolean isActive) {
        super(username, password, isActive, isActive, isActive, isActive, authorities);

        this.name = name;

    }

    public String getName() {
        return name;
    }



}

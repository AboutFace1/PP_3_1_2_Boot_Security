package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getUserById(int id);
    User getUserByName(String name);
    void save(User user);
    void update(int id, User updatedUser);
    void delete(int id);
}

package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByEmail(name).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(int id, User updatedUser) {
        userRepository.updateUser(updatedUser.getName(), updatedUser.getAge(), updatedUser.getEmail(), updatedUser.getRole(), id);
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}

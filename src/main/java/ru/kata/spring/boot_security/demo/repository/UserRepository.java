package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;
import java.util.Set;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Query("update User u set u.name = ?1, u.age = ?2, u.email = ?3, u.roles =?4 WHERE u.id = ?5")
    void updateUser(String name, Byte age, String email, Set<Role> role, Integer id);

    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);


}

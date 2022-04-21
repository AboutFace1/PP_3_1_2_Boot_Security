package ru.kata.spring.boot_security.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class RestUsersController {

    UserService userService;
    RoleService roleService;

    @Autowired
    public RestUsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAll();
        List<UserDTO> dtoUsers = users.stream().map(user -> toDTO(user)).collect(Collectors.toList());


        return new ResponseEntity<>(dtoUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(toDTO(user), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            userService.save(user);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.delete(id);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable int id) {
        userService.update(id, user);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

   @GetMapping("/authenticated-user")
   public UserDTO getUserPage(Principal principal) {

       return toDTO(userService.getUserByName(principal.getName()));
   }


   private UserDTO toDTO(User user) {
       Set<RoleDTO> roleDTOs = toDTOs(user.getRoles());

        return new UserDTO(user.getId(), user.getName(), user.getAge(), user.getEmail(), user.getPassword(), roleDTOs);
   }

   private Set<RoleDTO> toDTOs(Set<Role> roles) {
       return roles.stream().map(role -> toRoleDTO(role)).collect(Collectors.toSet());
   }

    private RoleDTO toRoleDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }
}

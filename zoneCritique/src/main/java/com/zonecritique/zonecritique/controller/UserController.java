package com.zonecritique.zonecritique.controller;

import com.zonecritique.zonecritique.dto.LoginRequest;
import com.zonecritique.zonecritique.entity.User;
import com.zonecritique.zonecritique.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
       if(userService.existsByEmail(user.getEmail())) {
           return ResponseEntity.badRequest().body("Email already exists");
       }
       if (userService.existsByUsername(user.getUsername())) {
           return ResponseEntity.badRequest().body("Username already exists");
       }

       User newUser = userService.saveUser(user);
       return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if(user != null && userService.authenticate(loginRequest.getPassword(), user.getPassword()))  {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }
}

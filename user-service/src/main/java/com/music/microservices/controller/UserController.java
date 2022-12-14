package com.music.microservices.controller;

import com.music.microservices.model.User;
import com.music.microservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @PostMapping(value="/register")
    public ResponseEntity<User> register(@RequestBody User user) throws Exception {
            userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@RequestBody User user) throws Exception {
             userService.login(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam String username){
        userService.deleteUser(username);
    }
}

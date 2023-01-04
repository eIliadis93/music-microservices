package com.music.microservices.controller;

import com.music.microservices.dto.SongRequest;
import com.music.microservices.model.User;
import com.music.microservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value ="/{username}/createSongList")
    public ResponseEntity<String> createSongList(@RequestBody SongRequest songRequest, @PathVariable String username){
        userService.createSongList(songRequest, username);
        return ResponseEntity.status(HttpStatus.OK)
                .body(songRequest.toString());
    }

    @DeleteMapping(value ="/{username}/deleteSongList")
    public ResponseEntity<String> deleteSongList(@RequestBody SongRequest songRequest, @PathVariable String username){
        userService.deleteSongList(songRequest, username);
        return ResponseEntity.status(HttpStatus.OK)
                .body(songRequest.toString());
    }

    @PostMapping(value="/register")
    public ResponseEntity<String> createUser(@RequestBody User user){
        String result = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody User user){
        String token = userService.login(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(token);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam String username){
        userService.deleteUser(username);
    }
}

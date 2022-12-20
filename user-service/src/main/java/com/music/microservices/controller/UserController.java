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

    @PostMapping(value ="/{id}/createSongList")
    public ResponseEntity<String> createSongList(@RequestBody SongRequest songRequest, @PathVariable Long id){
        userService.createSongList(songRequest, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(songRequest.toString());
    }

    @DeleteMapping(value ="/{id}/deleteSongList")
    public ResponseEntity<String> deleteSongList(@RequestBody SongRequest songRequest, @PathVariable Long id){
        userService.deleteSongList(songRequest, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(songRequest.toString());
    }

    @PostMapping(value="/register")
    public ResponseEntity<User> createUser(@RequestBody User user){
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestParam Long id){
        userService.updateUser(user,id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
    }
}

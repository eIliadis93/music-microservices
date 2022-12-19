package com.com.music.microservices.soundtrackservice.controller;

import com.com.music.microservices.soundtrackservice.dto.SoundtrackDto;
import com.com.music.microservices.soundtrackservice.model.Soundtrack;
import com.com.music.microservices.soundtrackservice.service.SoundtrackService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/soundtrack")
@RequiredArgsConstructor
public class SoundtrackController {

    private final SoundtrackService soundtrackService;


    @PostMapping(consumes = {"multipart/form-data"})
    @CircuitBreaker(name = "upload", fallbackMethod = "fallbackMethod")
    public ResponseEntity<?> upload(@RequestPart("soundtrackDto") SoundtrackDto soundtrackDto, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        String uploadSong = soundtrackService.uploadSoundtrack(multipartFile, soundtrackDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadSong);
    }

    @GetMapping(value = "/download/{fileName}" , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @CircuitBreaker(name = "download", fallbackMethod = "fallbackMethod")
    public HttpEntity<byte[]> download(@PathVariable String fileName) throws IOException {
        byte[] content = soundtrackService.downloadSoundtrack(fileName);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("audio", "mpeg"));
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName.replace(" ", "_"));
        header.setContentLength(content.length);
        return new HttpEntity<byte[]>(content, header);
    }

    @GetMapping("/allSongs")
    public ResponseEntity<List<Soundtrack>> getAllSongs() {
        return ResponseEntity.ok().body(soundtrackService.finalAllSoundtracks());
    }

    @GetMapping("findByName")
    public ResponseEntity<Soundtrack> getSongByName(@RequestParam String name) {
        return ResponseEntity.ok().body(soundtrackService.findSongByName(name));
    }

    public String fallbackMethod(SoundtrackDto soundtrackDto, RuntimeException runtimeException){
        return "Something went wrong please try again after some time";
    }
}

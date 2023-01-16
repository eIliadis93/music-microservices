package com.music.microservices.soundtrackservice.controller;

import com.music.microservices.soundtrackservice.dto.SoundtrackDto;
import com.music.microservices.soundtrackservice.model.Soundtrack;
import com.music.microservices.soundtrackservice.service.SoundtrackService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/soundtrack")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SoundtrackController {

    private final SoundtrackService soundtrackService;


    @PostMapping(consumes = {"*/*"})
    @CircuitBreaker(name = "soundtrack", fallbackMethod = "fallbackMethodUpload")
    @Retry(name = "soundtrack")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadSong = soundtrackService.uploadSoundtrack(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadSong);
    }

    @GetMapping(value = "/{fileName}" , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @CircuitBreaker(name = "soundtrack", fallbackMethod = "fallbackMethodDownload")
    @Retry(name = "soundtrack")
    public HttpEntity<byte[]> download(@PathVariable String fileName) throws IOException {
        byte[] content = soundtrackService.downloadSoundtrack(fileName);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("audio", "mpeg"));
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName.replace(" ", "_"));
        header.setContentLength(content.length);
        return new HttpEntity<byte[]>(content, header);
    }

    @GetMapping(value = "/stream/{fileName}")
    public ResponseEntity<byte[]> playSong(@PathVariable String fileName, HttpServletResponse response) {
        try {
            Soundtrack soundtrack = soundtrackService.findSongByName(fileName);
            Path path = Paths.get(soundtrack.getFilePath());
            response.setContentType("audio/mp3");
            response.setHeader("Content-Disposition", "inline; filename=\"" + soundtrack.getName() + "\"");
            response.setContentLength((int) Files.size(path));
            Files.copy(path, response.getOutputStream());
            response.flushBuffer();

        } catch (Exception ignored) {
        }
        return null;
    }

    @GetMapping("/allSongs")
    public ResponseEntity<List<Soundtrack>> getAllSongs() {
        HttpHeaders header = new HttpHeaders();
        return ResponseEntity.ok().body(soundtrackService.finalAllSoundtracks());
    }

    @GetMapping("findByName/{name}")
    public ResponseEntity<Soundtrack> getSongByName(@PathVariable String name) {
        return ResponseEntity.ok().body(soundtrackService.findSongByName(name));
    }

    public CompletableFuture<String> fallbackMethodUpload(SoundtrackDto soundtrackDto, MultipartFile multipartFile, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> "Too many upload requests are happening, we are sorry for this, please try again in some minutes.");
    }

    public CompletableFuture<String> fallbackMethodDownload(String fileName, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> "Too many download requests are happening, we are sorry for this, please try again in some minutes.");
    }
}

package com.soundtrackservice.controller;

import com.soundtrackservice.dto.SoundtrackDto;
import com.soundtrackservice.model.Soundtrack;
import com.soundtrackservice.service.SoundtrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SoundtrackController {

    private final SoundtrackService soundtrackService;


    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> upload(@RequestPart("soundtrackDto") SoundtrackDto soundtrackDto, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        String uploadSong = soundtrackService.uploadSoundtrack(multipartFile, soundtrackDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadSong);
    }

    @GetMapping(value = "/download/{fileName}" , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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
}

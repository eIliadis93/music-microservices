package com.music.microservices.soundtrackservice.service;

import com.music.microservices.soundtrackservice.dto.SoundtrackDto;
import com.music.microservices.soundtrackservice.event.SoundtrackFileEvent;
import com.music.microservices.soundtrackservice.model.Soundtrack;
import com.music.microservices.soundtrackservice.repository.SoundtrackRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SoundtrackService {

    private final SoundtrackRepository soundtrackRepository;
    private final String directory = "C:/Users/vagil/OneDrive/Υπολογιστής/MusicAppSongs/";
    private final Tracer tracer;
    private final KafkaTemplate<String, SoundtrackFileEvent> kafkaTemplate;

    public String uploadSoundtrack(MultipartFile multipartFile, SoundtrackDto soundtrackDto) throws IOException {

       Span soundtrackServiceLookup = tracer.nextSpan().name("SoundtrackServiceLookup");

       try(Tracer.SpanInScope spanInScope = tracer.withSpan(soundtrackServiceLookup.start())){

           String filePath = directory + multipartFile.getOriginalFilename();
           if(soundtrackRepository.findByName(multipartFile.getOriginalFilename())!=null){
               log.error("File with name " + multipartFile.getOriginalFilename() + " already exist");
               return "File with name " + multipartFile.getOriginalFilename() + " already exist";
           }
           else {
               Soundtrack soundtrack = Soundtrack.builder()
                       .name(multipartFile.getOriginalFilename())
                       .type(multipartFile.getContentType())
                       .filePath(filePath)
                       .artist(soundtrackDto.getArtist())
                       .album(soundtrackDto.getAlbum())
                       .build();
               soundtrackRepository.insert(soundtrack);
               kafkaTemplate.send("notificationTopic", new SoundtrackFileEvent(soundtrack.getName()));
               multipartFile.transferTo(new File(filePath));
               log.info("File Uploaded Successfully {}", filePath);
               return "File Uploaded Successfully" + filePath;
           }

       } finally {
           soundtrackServiceLookup.end();
       }

    }

    public byte[] downloadSoundtrack(String fileName) throws IOException {

        Span soundtrackServiceLookup = tracer.nextSpan().name("SoundtrackServiceLookup");

        try(Tracer.SpanInScope spanInScope = tracer.withSpan(soundtrackServiceLookup.start())) {

            Soundtrack soundData = soundtrackRepository.findByName(fileName);
            kafkaTemplate.send("notificationTopic", new SoundtrackFileEvent(fileName));
            String filePath = soundData.getFilePath();
            log.info("File {} downloaded", fileName);
            return Files.readAllBytes(new File(filePath).toPath());
        }finally {
            soundtrackServiceLookup.end();
        }
    }

    public List<Soundtrack> finalAllSoundtracks() {
        log.info("Getting all soundtracks");
        return soundtrackRepository.findAll();
    }

    public Soundtrack findSongByName(String name){
        log.info("Getting soundtrack with name {}", name);
        return soundtrackRepository.findByName(name);
    }
}

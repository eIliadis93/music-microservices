package com.soundtrackservice.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.soundtrackservice.dto.SoundtrackDto;
import com.soundtrackservice.model.Soundtrack;
import com.soundtrackservice.repository.SoundtrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
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

    public String uploadSoundtrack(MultipartFile multipartFile, SoundtrackDto soundtrackDto) throws IOException {
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
                    .image(soundtrackDto.getImage())
                    .build();

            soundtrackRepository.insert(soundtrack);
            multipartFile.transferTo(new File(filePath));
            log.info("File Uploaded Successfully {}", filePath);
            return "File Uploaded Successfully" + filePath;
        }
    }

    public byte[] downloadSoundtrack(String fileName) throws IOException {
        Soundtrack soundData = soundtrackRepository.findByName(fileName);
        String filePath = soundData.getFilePath();
        log.info("File {} downloaded", fileName);
        return Files.readAllBytes(new File(filePath).toPath());
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

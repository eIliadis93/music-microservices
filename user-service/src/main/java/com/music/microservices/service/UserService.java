package com.music.microservices.service;

import com.music.microservices.dto.SongRequest;
import com.music.microservices.dto.SongRequestItemsDto;
import com.music.microservices.event.UserFileEvent;
import com.music.microservices.model.Song;
import com.music.microservices.model.User;
import com.music.microservices.repository.UserRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final Tracer tracer;
    private final KafkaTemplate<String, UserFileEvent> kafkaTemplate;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64encoder = Base64.getUrlEncoder();

    public void createSongList(SongRequest songRequest, String username){

        Span soundtrackServiceLookup = tracer.nextSpan().name("SoundtrackServiceLookup");

        try(Tracer.SpanInScope spanInScope = tracer.withSpan(soundtrackServiceLookup.start())) {
            User user = userRepository.getReferenceById(username);
            List<Song> songList = songRequest.getSongRequestItemsDtoList()
                    .stream()
                    .map(this::mapToDto)
                    .toList();

            user.setSongsList(songList);
            userRepository.save(user);
            kafkaTemplate.send("notificationTopic", new UserFileEvent(user.getUserName()));
        }finally {
            soundtrackServiceLookup.end();
        }
    }

    public void deleteSongList(SongRequest songRequest, String username){

        Span soundtrackServiceLookup = tracer.nextSpan().name("SoundtrackServiceLookup");
        try(Tracer.SpanInScope spanInScope = tracer.withSpan(soundtrackServiceLookup.start())) {
            User user = userRepository.getReferenceById(username);
            List<Song> songList = songRequest.getSongRequestItemsDtoList()
                    .stream()
                    .map(this::mapToDto)
                    .toList();

            user.getSongsList().remove(songList);
            userRepository.save(user);
            kafkaTemplate.send("notificationTopic", new UserFileEvent(user.getUserName()));
        }finally {
        soundtrackServiceLookup.end();
        }
    }

    private Song mapToDto(SongRequestItemsDto songRequestItemsDto) {
        Song song = new Song();
        song.setSongName(songRequestItemsDto.getSongName());
        song.setType(songRequestItemsDto.getType());
        song.setFilePath(songRequestItemsDto.getFilePath());
        song.setArtist(songRequestItemsDto.getArtist());
        song.setAlbum(songRequestItemsDto.getAlbum());
        return song;
    }

    public void deleteUser(String username){
        User user = userRepository.getReferenceById(username);
        userRepository.delete(user);
    }

    public String register(User user) {
        //Check if there is a user with the same username
        if(checkUsername(user)){
            
            log.info("Creating the user.");
            
            user.setToken(generateToken());
            userRepository.save(user);

            return "User registration completed.";
        }
        else{
            return "This username already exist, please try another.";
        }
    }

    private String generateToken() {
        byte[] token = new byte[24];
        secureRandom.nextBytes(token);
        return base64encoder.encodeToString(token);
    }

    private boolean checkUsername(User user) {
        User exist = userRepository.findById(user.getUserName()).orElse(null);

        if(exist == null)
            return false;
        return true;
    }

    public String login(User user) {
        User existingUser = userRepository.findById(user.getUserName()).orElse(null);

        if(existingUser == null){
            return "User is not present.";
        }
        else return existingUser.getToken();
    }
}

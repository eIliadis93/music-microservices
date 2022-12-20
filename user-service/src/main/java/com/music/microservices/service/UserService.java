package com.music.microservices.service;

import com.music.microservices.dto.SongRequest;
import com.music.microservices.dto.SongRequestItemsDto;
import com.music.microservices.event.UserFileEvent;
import com.music.microservices.model.Song;
import com.music.microservices.model.User;
import com.music.microservices.repository.UserRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Tracer tracer;
    private final KafkaTemplate<String, UserFileEvent> kafkaTemplate;

    public void createSongList(SongRequest songRequest, Long id){

        Span soundtrackServiceLookup = tracer.nextSpan().name("SoundtrackServiceLookup");

        try(Tracer.SpanInScope spanInScope = tracer.withSpan(soundtrackServiceLookup.start())) {
            User user = userRepository.getReferenceById(id);
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

    public void deleteSongList(SongRequest songRequest, Long id){

        Span soundtrackServiceLookup = tracer.nextSpan().name("SoundtrackServiceLookup");
        try(Tracer.SpanInScope spanInScope = tracer.withSpan(soundtrackServiceLookup.start())) {
            User user = userRepository.getReferenceById(id);
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

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(User user,Long id){
            userRepository.getReferenceById(id).setUserName(user.getUserName());
            userRepository.getReferenceById(id).setPassword(user.getPassword());
        return userRepository.save(userRepository.getReferenceById(id));
    }

    public void deleteUser(Long id){
        User user = userRepository.getReferenceById(id);
        userRepository.delete(user);
    }
}

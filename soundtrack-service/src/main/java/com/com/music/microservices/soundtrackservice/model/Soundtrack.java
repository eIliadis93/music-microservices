package com.com.music.microservices.soundtrackservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "song-service")
public class Soundtrack {

    @Id
    @Field
    private String id;
    @Field
    private String name;
    @Field
    private String type;
    @Field
    private String filePath;
    @Field
    private String artist;
    @Field
    private String album;
    @Field
    private String image;
}

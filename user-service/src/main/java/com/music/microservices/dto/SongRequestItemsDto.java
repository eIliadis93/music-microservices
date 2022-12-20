package com.music.microservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongRequestItemsDto {
    private Long id;
    private String songName;
    private String type;
    private String filePath;
    private String artist;
    private String album;
}

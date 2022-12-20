package com.music.microservices.soundtrackservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoundtrackFileEvent {
    private String soundTrackName;
}

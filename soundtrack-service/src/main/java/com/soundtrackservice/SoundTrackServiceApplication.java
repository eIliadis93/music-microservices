package com.soundtrackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SoundTrackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoundTrackServiceApplication.class, args);
    }

}

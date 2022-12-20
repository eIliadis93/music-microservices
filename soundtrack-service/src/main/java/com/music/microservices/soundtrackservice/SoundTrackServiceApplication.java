package com.music.microservices.soundtrackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@PropertySource("classpath:application.properties")
public class SoundTrackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoundTrackServiceApplication.class, args);
    }

}

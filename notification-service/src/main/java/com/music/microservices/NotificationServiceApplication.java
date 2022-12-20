package com.music.microservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(SoundtrackFileEvent soundtrackFileEvent){
        //this class here can be used to add any notification when the event triggers like an email
        //for this project we will just use logging info
        log.info("Notification for Soundtrack {} received.",soundtrackFileEvent.getSoundTrackName());
    }
}


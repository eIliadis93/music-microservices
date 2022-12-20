package com.music.microservices.soundtrackservice.repository;

import com.music.microservices.soundtrackservice.model.Soundtrack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundtrackRepository extends MongoRepository<Soundtrack, String> {
    Soundtrack findByName(String fileName);
}

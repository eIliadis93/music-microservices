package com.soundtrackservice.repository;

import com.soundtrackservice.model.Soundtrack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoundtrackRepository extends MongoRepository<Soundtrack, String> {
    Soundtrack findByName(String fileName);
}

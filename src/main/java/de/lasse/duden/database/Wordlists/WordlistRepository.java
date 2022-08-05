package de.lasse.duden.database.Wordlists;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WordlistRepository extends MongoRepository<Wordlist, String> {

    Wordlist findWordlistById(String id);
    List<Wordlist> findWordlistByOwner(String owner);

}

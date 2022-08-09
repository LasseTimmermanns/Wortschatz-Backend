package de.lasse.duden.database.Wordlists;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WordlistInterfaceRepository extends MongoRepository<Wordlist, String> {

    public Wordlist findWordlistById(String id);

    public List<Wordlist> findWordlistsByOwner(String owner);

    public List<Wordlist> findWordlistsByOwnerNot(String owner);

    public List<Wordlist> findAll();
}



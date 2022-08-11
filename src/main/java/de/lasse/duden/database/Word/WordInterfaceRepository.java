package de.lasse.duden.database.Word;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordInterfaceRepository extends MongoRepository<Word, String> {

    public Word findWordById(String word);
    public Word findWordByWord(String id);

}

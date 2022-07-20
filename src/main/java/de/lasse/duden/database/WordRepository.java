package de.lasse.duden.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends CrudRepository<Word, String> {

    public Word findByWord(String word);
    public List<Word> findAll();

}

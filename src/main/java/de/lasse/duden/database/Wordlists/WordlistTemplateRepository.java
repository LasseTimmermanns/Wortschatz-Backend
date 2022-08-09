package de.lasse.duden.database.Wordlists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class WordlistTemplateRepository {

    @Autowired
    MongoTemplate mongoTemplate;


}

package de.lasse.duden.database.Wordlists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomWordlistRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public void addWordToWordlist(String wordlistId, String word, String subject){
        Update update = new Update();
        update.addToSet("words", word);
        Criteria criteria = Criteria.where("_id").is(wordlistId);
        criteria = criteria.and("owner").is(subject);
        mongoTemplate.updateFirst(Query.query(criteria), update, "Wordlists");
    }

    public List<WordlistDisplay> getUserWordlists(String subject){
        Criteria criteria = Criteria.where("owner").is(subject);
        Query query = new Query(criteria);
        query.fields().include("_id").include("name");
        return mongoTemplate.find(query, WordlistDisplay.class);

    }
}

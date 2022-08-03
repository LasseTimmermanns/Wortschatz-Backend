package de.lasse.duden.database.Utilization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilizationRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<UtilizationDisplay> getUtilizations(int limit){
        Query query = new Query().limit(limit);
        return mongoTemplate.find(query, UtilizationDisplay.class, "UtilizationCounts");
    }


}

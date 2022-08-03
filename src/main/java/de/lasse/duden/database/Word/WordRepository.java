package de.lasse.duden.database.Word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class WordRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Word> getWordsWithFilter(Collection<String> utilization, Collection<String> kind, int frequency, int limit){
        Query query = new Query().limit(limit);
        Criteria criteria = new Criteria();

        if(frequency > 1)
            query.addCriteria(criteria.and("frequency").gte(frequency));

        if(utilization.size() > 0)
            query.addCriteria(expandCriteria(criteria,"utilization", utilization));

        if(kind.size() > 0)
            query.addCriteria(expandCriteria(criteria,"kind", kind));

        MatchOperation matchOperation = Aggregation.match(criteria);
        SampleOperation sampleOperation = Aggregation.sample(limit);
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sampleOperation);
        AggregationResults<Word> results = mongoTemplate.aggregate(aggregation, "Dictionary", Word.class);
        return results.getMappedResults();
    }

    private Criteria expandCriteria(Criteria criteria, String name, Collection<String> value){
        return criteria.and(name).in(value);
    }

}

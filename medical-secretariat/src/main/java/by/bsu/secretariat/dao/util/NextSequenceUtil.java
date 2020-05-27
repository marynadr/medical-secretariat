package by.bsu.secretariat.dao.util;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


@Service
public class NextSequenceUtil {

    public NextSequenceUtil(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }
    private MongoTemplate mongoTemplate;

    //db.sequence.insert({_id: "customSequences",seq: 0})

    public int getNextSequence(String seqName)
    {
        CustomSequences counter = mongoTemplate.findAndModify(
                query(where("_id").is(seqName)),
                new Update().inc("seq",1),
                options().returnNew(true).upsert(true),
                CustomSequences.class);
        return counter.getSeq();
    }

    private static String SEQUENCE_COLLECTION_NAME = "sequence";
    public int getNextSequence() {
        CustomSequences counter = mongoTemplate.findAndModify(
                query(where("_id").is(SEQUENCE_COLLECTION_NAME)),
                new Update().inc("seq",1),
                options().returnNew(true).upsert(true),
                CustomSequences.class);
        return counter.getSeq();
    }
}
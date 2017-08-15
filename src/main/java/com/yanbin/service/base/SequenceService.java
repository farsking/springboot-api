package com.yanbin.service.base;

import com.google.common.collect.Lists;
import com.yanbin.model.enums.SeqType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by yanbin on 2017/7/8.
 */
@Service
public class SequenceService implements ISequence {

    private MongoTemplate mongoTemplate;

    @Autowired
    public SequenceService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Long newKey(SeqType seqType) {
        String key = seqType.getKey();
        initKey(key);
        Query query = Query.query(Criteria.where("key").is(key));
        Update update = new Update().inc("nextValue", 1);
        Sequence sequence = mongoTemplate.findAndModify(query, update, Sequence.class);
        return sequence.getNextValue();
    }

    private void initKey(String key) {
        Query query = Query.query(Criteria.where("key").is(key));
        List<Sequence> sequenceList = mongoTemplate.find(query,Sequence.class);
        if (CollectionUtils.isEmpty(sequenceList)){
            mongoTemplate.insert(new Sequence(key,10000L));
        }
    }

    @Override
    public List<Long> newKeys(SeqType seqType, int size) {
        String key = seqType.getKey();
        initKey(key);
        Query query = Query.query(Criteria.where("table").is(key));
        Update update = new Update().inc("nextValue", size);
        Sequence sequence = mongoTemplate.findAndModify(query, update, Sequence.class);
        List<Long> result = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            result.add(sequence.getNextValue() + i);
        }
        return result;
    }
}

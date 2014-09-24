package net.github.rtc.quiz.dao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.DB;
import com.mongodb.Mongo;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.jongo.marshall.jackson.JacksonMapper.Builder;

import javax.annotation.PostConstruct;

/**
 * Created by Ivan Yatcuba on 9/24/14.
 */
public abstract class AbstractMongoDAO<T> {
    @Autowired
    private Mongo mongo;

    private Jongo jongo;
    protected MongoCollection collection;


    @PostConstruct
    public final void init() {
        String dbName = "mydb";
        DB db = this.mongo.getDB(dbName);
        this.jongo = this.createJongo(db);
        this.collection = this.jongo.getCollection(this.getCollectionName());
    }

    protected Jongo createJongo(DB db) {
        Builder builder = new JacksonMapper.Builder();
        builder.enable(MapperFeature.AUTO_DETECT_GETTERS);
        builder.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return new Jongo(db, builder.build());
    }

    protected String getCollectionName() {
        return this.getEntityClass().getSimpleName();
    }

    protected abstract Class<T> getEntityClass();
}

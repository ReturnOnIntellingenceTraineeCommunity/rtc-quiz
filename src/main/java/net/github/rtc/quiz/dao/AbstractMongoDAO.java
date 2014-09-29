package net.github.rtc.quiz.dao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.DB;
import com.mongodb.Mongo;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.jongo.marshall.jackson.JacksonMapper.Builder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMongoDAO<T> implements GenericDAO<T> {
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

    protected final List<T> convertIterable(Iterable<T> as) {
        List<T> objects = new ArrayList<>();
        for (T mp : as) {
            objects.add(mp);
        }
        return objects;
    }

    public List<T> findAll(){
        Iterable<T> as = this.collection.find().sort("{_id:1}").as(this.getEntityClass());
        return this.convertIterable(as);
    }

    public void save(T item){
        this.collection.save(item);
    }

    public void update(String id, T item){
        this.collection.update(new ObjectId()).with(item);
    }

    public void delete(String id){
        this.collection.remove(new ObjectId(id));
    }

    public long getCount(){
        return this.collection.count();
    }

    public T getById(String id){
        return this.collection.findOne(new ObjectId(id)).as(getEntityClass());
    }
}

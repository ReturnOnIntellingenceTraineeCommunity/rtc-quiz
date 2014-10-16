package net.github.rtc.quiz.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Arrays;

@Configuration
class MongoConfiguration extends AbstractMongoConfiguration {
    @Value("#{systemEnvironment['MONGO_HOST']}")
    String mongoHost = "localhost";
    @Value("#{systemEnvironment['MONGO_PORT']}")
    String mongoPort = "27017";
    @Value("#{systemEnvironment['MONGO_DB_NAME']}")
    String mongoDbName = "gvm";
    @Value("#{systemEnvironment['MONGO_USERNAME']}")
    String mongoUsername;
    @Value("#{systemEnvironment['MONGO_PASSWORD']}")
    String mongoPassword;

    @Override
    protected String getDatabaseName() {
        return mongoDbName;
    }

    @Override
    public Mongo mongo() throws Exception {
        ServerAddress serverAddress = new ServerAddress(mongoHost, Integer.parseInt(mongoPort));
        if (mongoUsername != null && mongoPassword != null) {
            MongoCredential credential = MongoCredential.createMongoCRCredential(mongoUsername, mongoDbName,
              mongoPassword.toCharArray());
            return  new MongoClient(serverAddress, Arrays.asList(credential));
        } else {
            return new MongoClient(serverAddress);
        }
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MappingMongoConverter converter = new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(mongoDbFactory(), converter);
    }
}

package by.bsu.secretariat.dao.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
public class MongoDBConfig {
    public String getDatabaseName() {
        return "medical";
    }
    @Bean
    public MongoClient mongoClient() {
        ServerAddress address = new ServerAddress("127.0.0.1", 27017);
        MongoClientOptions options = new MongoClientOptions.Builder().build();
        MongoClient client = new MongoClient(address, options);
        return client;
    }
    @Bean
    public MongoDbFactory mongoDbFactory() {
        MongoDbFactory factory = new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
        return factory;
    }
    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoDbFactory());
        return template;
    }
}
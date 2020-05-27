package by.bsu.secretariat.dao.repository;

import by.bsu.secretariat.dao.entity.User;
import by.bsu.secretariat.exceptions.NoSuchElementInDatasourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class UsersRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String USERS_COLLECTION_NAME = "users";

    public List<User> getUsersByRole(String role){
        Query query = new Query();
        query.addCriteria(Criteria.where("role").is(role));
        Object o = mongoTemplate.find(query,User.class);
        return (ArrayList<User>) o;
    }

    public User getUserById(long id){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Object o = mongoTemplate.find(query,User.class);
        User user = ((ArrayList<User>) o).stream().findFirst().orElseThrow(() -> new NoSuchElementInDatasourceException("User with such id isn't found"));
        return user;
    }
}

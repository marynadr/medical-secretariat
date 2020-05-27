package by.bsu.secretariat.dao.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sequence")
@Getter
@Setter
public class CustomSequences {
    @Id
    private String id;
    private int seq;

// getters and setters
}

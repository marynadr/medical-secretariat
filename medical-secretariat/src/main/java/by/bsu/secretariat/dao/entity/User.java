package by.bsu.secretariat.dao.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "users")
public class User {
    @Id
    private long id;
    private String role;
    private String additonalInfo;
}

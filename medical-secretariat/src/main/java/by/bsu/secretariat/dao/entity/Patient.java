package by.bsu.secretariat.dao.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Document(collection = "patients")
public class Patient {
    @Id
    private long id;

    private Long doctorId;
    private Long visitId;

    private String status;
    private LocalDate visitDate;

    private LocalDate birth;
    private String firstName;
    private String secondName;
    private String additionalName;
    private String address;
}

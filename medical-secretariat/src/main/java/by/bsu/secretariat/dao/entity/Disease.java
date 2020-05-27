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
@Document(collection = "diseases")
public class Disease {
    @Id
    private long id;

    private long patientId;
    private long visitId;
    private LocalDate visitDate;
    private String diagnose;
    private String ward;
    private long mainDoctorId;
    private Treatment[] treatments;
    private boolean isActive;
}

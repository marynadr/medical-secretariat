package by.bsu.secretariat.controllers.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PatientDTO {
    private LocalDate birth;
    private String firstName;
    private String secondName;
    private String additionalName;
    private String address;
}

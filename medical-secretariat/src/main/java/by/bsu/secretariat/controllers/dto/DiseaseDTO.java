package by.bsu.secretariat.controllers.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DiseaseDTO {
    private String diagnose;
    private long patientId;
}

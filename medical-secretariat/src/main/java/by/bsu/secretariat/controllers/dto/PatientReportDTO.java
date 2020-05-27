package by.bsu.secretariat.controllers.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PatientReportDTO {
    private long id;
    private String name;
    private String address;
    private LocalDate birth;
}

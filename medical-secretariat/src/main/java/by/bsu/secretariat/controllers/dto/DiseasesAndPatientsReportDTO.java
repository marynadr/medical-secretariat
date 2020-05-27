package by.bsu.secretariat.controllers.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DiseasesAndPatientsReportDTO {
    private long patientId;
    private String patientName;
    private LocalDate patientBirth;
    private List<DiseaseReportDTO> diseases;
}

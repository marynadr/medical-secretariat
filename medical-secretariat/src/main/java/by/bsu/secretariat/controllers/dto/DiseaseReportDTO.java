package by.bsu.secretariat.controllers.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DiseaseReportDTO {
    private String diagnose;
    private long visitId;
    private LocalDate visitDate;
    private String ward;
    private long doctorId;
    private String doctorInfo;
    private List<TreatmentReportDTO> treatments;
}

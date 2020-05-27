package by.bsu.secretariat.controllers.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TreatmentReportDTO {
    private String date;
    private String time;
    private String treatmentName;
    private String notes;
    private Long doctorId;
}

package by.bsu.secretariat.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TreatmentDTO {
    private String treatmentName;
    private String datetime;
    private String notes;
    private long patientId;
}
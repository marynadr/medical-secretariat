package by.bsu.secretariat.dao.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Treatment {
    private Long doctorId;
    private String treatmentName;
    private LocalDateTime datetime;
    private String notes;
}

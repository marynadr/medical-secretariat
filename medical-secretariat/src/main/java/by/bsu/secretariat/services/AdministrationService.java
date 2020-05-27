package by.bsu.secretariat.services;

import by.bsu.secretariat.controllers.dto.*;
import by.bsu.secretariat.dao.entity.Disease;
import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.entity.Treatment;
import by.bsu.secretariat.dao.entity.User;
import by.bsu.secretariat.dao.repository.DiseasesRepository;
import by.bsu.secretariat.dao.repository.PatientsRepository;
import by.bsu.secretariat.dao.repository.UsersRepository;
import by.bsu.secretariat.services.util.PatientNameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministrationService {

    @Autowired
    private DiseasesRepository diseasesRepository;
    @Autowired
    private PatientsRepository patientsRepository;
    @Autowired
    private UsersRepository userRepository;

    public List<PatientReportDTO> generatePatientsReport(LocalDate start, LocalDate finish){
        if (finish.isBefore(start)) {
            throw new IllegalArgumentException("finish date is earlier than start one");
        }

        List<Patient> patients = patientsRepository.getPatientsByPeriod(start, finish);

        List<PatientReportDTO> report = patients.stream()
                .map(patient -> PatientReportDTO.builder()
                        .id(patient.getId())
//                        .visitId(patient.getVisitId())
                        .address(patient.getAddress())
                        .birth(patient.getBirth())
                        .name(PatientNameUtil.generateName(patient))
                        .build()
                ).collect(Collectors.toList());
        return report;
    }

    public List<DiseasesAndPatientsReportDTO> generateDiseasesReport(LocalDate start, LocalDate finish){
        if (finish.isBefore(start)) {
            throw new IllegalArgumentException("finish date is earlier than start one");
        }

        List<Patient> patients = patientsRepository.getPatientsByPeriod(start, finish);
        List<DiseasesAndPatientsReportDTO> diseasesAndPatientsReport = new ArrayList<>();

        for(Patient patient : patients) {
            List<Disease> diseases = diseasesRepository.getDiseasesByPatientId(patient.getId());
            List<DiseaseReportDTO> diseaseReportDTOList = new ArrayList<>();

            for(Disease disease : diseases){
                User doctor = userRepository.getUserById(disease.getMainDoctorId());

                DiseaseReportDTO diseaseReportDTO =DiseaseReportDTO.builder()
                        .diagnose(disease.getDiagnose())
                        .visitId(disease.getVisitId())
                        .visitDate(disease.getVisitDate())
                        .ward(disease.getWard())
                        .doctorId(disease.getMainDoctorId())
                        .doctorInfo(doctor.getAdditonalInfo())
                        .treatments(new ArrayList<>())
                        .build();

                for(Treatment treatment : disease.getTreatments()){
                    TreatmentReportDTO treatmentDTO = TreatmentReportDTO.builder()
                            .treatmentName(treatment.getTreatmentName())
                            .date(treatment.getDatetime().toLocalDate().toString())
                            .time(treatment.getDatetime().toLocalTime().toString())
                            .notes(treatment.getNotes())
                            .doctorId(treatment.getDoctorId())
                            .build();
                    diseaseReportDTO.getTreatments().add(treatmentDTO);
                }

                diseaseReportDTOList.add(diseaseReportDTO);
            }


            DiseasesAndPatientsReportDTO dto = DiseasesAndPatientsReportDTO.builder()
                    .patientId(patient.getId())
                    .patientName(PatientNameUtil.generateName(patient))
                    .patientBirth(patient.getBirth())
                    .diseases(diseaseReportDTOList)
                    .build();
            diseasesAndPatientsReport.add(dto);
        }

        return diseasesAndPatientsReport;
    }

    public List<PatientReportDTO> getPatientsByStatus(String status){
        List<Patient> patients = patientsRepository.getPatientsByStatus(status);

        List<PatientReportDTO> report = patients.stream()
                .map(patient -> PatientReportDTO.builder()
                                .id(patient.getId())
//                        .visitId(patient.getVisitId())
                                .address(patient.getAddress())
                                .birth(patient.getBirth())
                                .name(PatientNameUtil.generateName(patient))
                                .build()
                ).collect(Collectors.toList());

        return report;
    }



}

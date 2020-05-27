package by.bsu.secretariat.services;

import by.bsu.secretariat.controllers.dto.DiseaseDTO;
import by.bsu.secretariat.controllers.dto.TreatmentDTO;
import by.bsu.secretariat.dao.entity.Disease;
import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.entity.Treatment;
import by.bsu.secretariat.dao.repository.DiseasesRepository;
import by.bsu.secretariat.dao.repository.PatientsRepository;
import by.bsu.secretariat.dao.repository.UsersRepository;
import by.bsu.secretariat.exceptions.InvalidModificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private PatientsRepository patientsRepository;
    @Autowired
    private DiseasesRepository diseasesRepository;
    @Autowired
    private UsersRepository usersRepository;

    public boolean checkDoctorId(Long doctorId){
        try {
            if (doctorId != null) {
                if ("DOCTOR".equals(usersRepository.getUserById(doctorId).getRole())){
                    return true;
                }
            }
            return false;
        }catch (Exception ex){
            return false;
        }
    }

    public List<Patient> getReadyToVisitPatients(long doctorId){
        return patientsRepository.getByStatusAndDoctorId("ASSIGNED_TO_DOCTOR", doctorId);
    }

    public void createDiseaseAndUpdatePatient(DiseaseDTO diseaseDTO, long doctorId) throws InvalidModificationException{
        Patient patient = patientsRepository.getPatientById(diseaseDTO.getPatientId());

        if(! "ASSIGNED_TO_DOCTOR".equals(patient.getStatus())){
            throw new InvalidModificationException("Unable to hospitalize such patient, patient state is " + patient.getStatus());
        }
        if(patient.getDoctorId() == null || doctorId != patient.getDoctorId()){
            throw new InvalidModificationException("Unable to hospitalize patient because it isn't assigned to current doctor");
        }

        Disease disease = Disease.builder()
                .patientId(diseaseDTO.getPatientId())
                .visitId(patient.getVisitId())
                .visitDate(LocalDate.now())
                .diagnose(diseaseDTO.getDiagnose())
                .mainDoctorId(doctorId)
                .treatments(new Treatment[0])
                .isActive(true)
                .build();

        diseasesRepository.createNewDisease(disease);
        patientsRepository.setDoctorIdAndStatusAndGenerateVisitId(patient.getId(), doctorId, "DOCTOR_VISITED");
    }

    public List<Patient> getHospitalizedPatients(){
        return patientsRepository.getPatientsByStatus("HOSPITALIZED");
    }

    public List<Patient> getAssignedPatients(long doctorId){
        return patientsRepository.getByStatusAndDoctorId("HOSPITALIZED", doctorId);
    }

    public List<Patient> getPatientsByPeriod(LocalDate start, LocalDate finish){
        return patientsRepository.getPatientsByPeriod(start, finish);
    }

    public List<Patient> getPatientsByActiveDiagnose(String diagnose){
        List<Disease> diseases = diseasesRepository.getActiveDiseasesByDiagnose(diagnose);

        Set<Long> patients = diseases.stream().map(Disease::getPatientId).collect(Collectors.toSet());

        return patientsRepository.getPatientsFromIdList(patients);
    }

    public Disease getActiveDiseaseOfPatient(long patientId){
        return diseasesRepository.getActiveDiseaseByPatientId(patientId);
    }

    public List<Disease> getAllDiseasesOfPatient(long patientId){
        return diseasesRepository.getDiseasesByPatientId(patientId);
    }

    public void dischargePatient(long patientId) throws InvalidModificationException{
        Patient patient = patientsRepository.getPatientById(patientId);

        if(! "HOSPITALIZED".equals(patient.getStatus())){
            throw new InvalidModificationException("Unable to discharge such patient, patient state is " + patient.getStatus());
        }

        patientsRepository.setPatientStatus(patientId, "READY_TO_LEAVE");
    }

    public void addTreatment(TreatmentDTO treatmentDTO, long doctorId) throws InvalidModificationException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(treatmentDTO.getDatetime(), formatter);

        Patient patient = patientsRepository.getPatientById(treatmentDTO.getPatientId());
        if(! "HOSPITALIZED".equals(patient.getStatus())){
            throw new InvalidModificationException("Unable to assign treatment to such patient, patient state is " + patient.getStatus());
        }

        Treatment treatment = Treatment.builder()
                .treatmentName(treatmentDTO.getTreatmentName())
                .datetime(formatDateTime)
                .notes(treatmentDTO.getNotes())
                .doctorId(doctorId)
                .build();
         Disease disease = diseasesRepository.getActiveDiseaseByPatientId(treatmentDTO.getPatientId());
         if(disease.getTreatments() == null){
             Treatment[] emptyArr = new Treatment[0];
             disease.setTreatments(emptyArr);
         }

        diseasesRepository.addTreatmentToDisease(disease, treatment);
    }
}

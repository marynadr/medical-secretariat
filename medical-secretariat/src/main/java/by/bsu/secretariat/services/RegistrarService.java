package by.bsu.secretariat.services;

import by.bsu.secretariat.controllers.dto.PatientDTO;
import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.repository.DiseasesRepository;
import by.bsu.secretariat.dao.repository.PatientsRepository;
import by.bsu.secretariat.exceptions.InvalidModificationException;
import by.bsu.secretariat.exceptions.NoSuchElementInDatasourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

@Service
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegistrarService {

    @Autowired
    private PatientsRepository patientsRepository;
    @Autowired
    private DiseasesRepository diseasesRepository;

    public List<Patient> getPatientsByPeriod(LocalDate start, LocalDate finish){
        return patientsRepository.getPatientsByPeriod(start, finish);
    }

    public List<Patient> getAllPatients(){
        return patientsRepository.getAll();
    }

    public Patient getPatientById(long id){
        return patientsRepository.getPatientById(id);
    }

    public List<Patient> getPatientsByStatus(String status){
        return patientsRepository.getPatientsByStatus(status);
    }

    public void registerNewPatient(PatientDTO patientDTO){
        Patient p = Patient.builder()
                .birth(patientDTO.getBirth())
                .firstName(patientDTO.getFirstName())
                .secondName(patientDTO.getSecondName())
                .additionalName(patientDTO.getAdditionalName())
                .address(patientDTO.getAddress())
                .status("REGISTERED")
                .build();
        patientsRepository.createNewPatient(p);
    }

    public void registerExistingPatient(long patientId) throws InvalidModificationException{
        Patient patient = patientsRepository.getPatientById(patientId);
        if(! "LEAVED".equals(patient.getStatus())) {
            throw new InvalidModificationException("Unable to register existing patient, patient state is " + patient.getStatus());
        }
        patientsRepository.setPatientStatus(patientId, "REGISTERED");
    }


    public void dischargePatientById(long id) throws InvalidModificationException{
        Patient patient = patientsRepository.getPatientById(id);
        if("READY_TO_LEAVE".equals(patient.getStatus())) {
            patientsRepository.setPatientStatus(id, "LEAVED");
            patientsRepository.setDoctorIdAndWardAndVisitId(id, null, null, null);
            diseasesRepository.deactivateDiseaseByPatientId(patient.getId());
        } else {
          throw new InvalidModificationException("Unable to discharge patient, patient state is " + patient.getStatus());
        }
    }
}

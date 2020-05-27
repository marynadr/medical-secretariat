package by.bsu.secretariat.services;

import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.entity.User;
import by.bsu.secretariat.dao.repository.DiseasesRepository;
import by.bsu.secretariat.dao.repository.PatientsRepository;
import by.bsu.secretariat.dao.repository.UsersRepository;
import by.bsu.secretariat.exceptions.InvalidModificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceptionistService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PatientsRepository patientsRepository;
    @Autowired
    private DiseasesRepository diseasesRepository;

    public List<User> getAllDoctors(){
        return usersRepository.getUsersByRole("DOCTOR");
    }

    public List<Patient> getPatientsByStatus(String status){
        return patientsRepository.getPatientsByStatus(status);
    }

    public void sendPatientToDoctor(long patient_id, long doctor_id) throws InvalidModificationException{
        Patient patient = patientsRepository.getPatientById(patient_id);
        if(! "REGISTERED".equals(patient.getStatus())){
            throw new InvalidModificationException("Unable to assign patient to doctor, patient state is " + patient.getStatus());
        }

        User doctor = usersRepository.getUserById(doctor_id);

        patientsRepository.setDoctorIdAndStatusAndGenerateVisitId(patient_id, doctor_id, "ASSIGNED_TO_DOCTOR");
    }

    public void hospitalizePatient(long patient_id, String ward) throws InvalidModificationException{
        Patient patient = patientsRepository.getPatientById(patient_id);
        if(! "DOCTOR_VISITED".equals(patient.getStatus())){
            throw new InvalidModificationException("Unable to hospitalize such patient, patient state is " + patient.getStatus());
        }

        patientsRepository.setPatientStatus(patient.getId(), "HOSPITALIZED");
        diseasesRepository.setWardByPatientId(patient_id, ward);
    }
}

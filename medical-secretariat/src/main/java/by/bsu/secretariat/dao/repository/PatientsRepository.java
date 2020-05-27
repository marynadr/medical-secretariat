package by.bsu.secretariat.dao.repository;

import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.entity.User;
import by.bsu.secretariat.dao.util.NextSequenceUtil;
import by.bsu.secretariat.exceptions.NoSuchElementInDatasourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class PatientsRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String PATIENTS_COLLECTION_NAME = "patients";

    public List<Patient> getPatientsByPeriod(LocalDate start, LocalDate finish){
        Query query = new Query();
        query.addCriteria(Criteria.where("visitDate").gte(start).lte(finish));
        Object o = mongoTemplate.find(query,Patient.class);

        List<Patient> patients = ((ArrayList<Patient>) o);
        return patients;
    }

    public Patient getPatientById(long id){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Object o = mongoTemplate.find(query,Patient.class);
        Patient patient = ((ArrayList<Patient>) o).stream().findFirst().orElseThrow(() -> new NoSuchElementInDatasourceException("Patient with such id isn't found"));
        return patient;
    }

    public List<Patient> getPatientsByStatus(String status){
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));
        Object o = mongoTemplate.find(query,Patient.class);
        List<Patient> patients = ((ArrayList<Patient>) o);
        return patients;
    }

    public void createNewPatient(Patient patient){
        NextSequenceUtil util = new NextSequenceUtil(mongoTemplate);

        patient.setId(util.getNextSequence());
        Query query = new Query();
        mongoTemplate.insert(patient,PATIENTS_COLLECTION_NAME );
    }

    public void setPatientStatus(long id, String status){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("status", status);
        mongoTemplate.findAndModify(query, update, Patient.class);
    }

    public void setDoctorIdAndStatusAndGenerateVisitId(long patient_id, long doctor_id, String status){
        NextSequenceUtil util = new NextSequenceUtil(mongoTemplate);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(patient_id));
        Update update = new Update();
        update.set("doctorId", doctor_id);
        update.set("status", status);
        update.set("visitId", util.getNextSequence());
        update.set("visitDate", LocalDate.now());
        mongoTemplate.findAndModify(query, update, Patient.class);
    }

    public boolean deletePatient(long id){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        if(mongoTemplate.remove(query, PATIENTS_COLLECTION_NAME).getDeletedCount() == 0){
            return false;
        }
        return true;
    }

    public List<Patient> getByStatusAndDoctorId(String status, long doctorId){
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));
        query.addCriteria(Criteria.where("doctorId").is(doctorId));
        Object o = mongoTemplate.find(query,Patient.class);
        return (ArrayList<Patient>) o;
    }

    public List<Patient> getPatientsFromIdList(Collection<Long> ids){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Object o = mongoTemplate.find(query,Patient.class);
        return (List<Patient>) o;
    }

    public void setDoctorIdAndWardAndVisitId(long patientId, Long doctorId, String ward, Long visitId){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(patientId));

        Update update = new Update();
        update.set("doctorId", doctorId);
        update.set("ward", ward);
        update.set("visitId", visitId);
        mongoTemplate.findAndModify(query, update, Patient.class);
    }

    public List<Patient> getAll(){
        Query query = new Query();
        Object o =mongoTemplate.findAll(Patient.class);
        List<Patient> patients = ((ArrayList<Patient>) o);
        return patients;
    }
}

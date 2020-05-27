package by.bsu.secretariat.dao.repository;

import by.bsu.secretariat.dao.entity.Disease;
import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.entity.Treatment;
import by.bsu.secretariat.dao.util.NextSequenceUtil;
import by.bsu.secretariat.exceptions.NoSuchElementInDatasourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class DiseasesRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String DISEASES_COLLECTION_NAME = "diseases";

    public void createNewDisease(Disease disease){
        NextSequenceUtil util = new NextSequenceUtil(mongoTemplate);
        disease.setId(util.getNextSequence());

        mongoTemplate.insert(disease, DISEASES_COLLECTION_NAME);
    }

    public List<Disease> getActiveDiseasesByDiagnose(String diagnose){
        Query query = new Query();
        query.addCriteria(Criteria.where("diagnose").is(diagnose));
        query.addCriteria(Criteria.where("isActive").is(true));
        Object o = mongoTemplate.find(query,Disease.class);
        return (List<Disease>) o;
    }

    public Disease getActiveDiseaseByPatientId(long patientId){
        Query query = new Query();
        query.addCriteria(Criteria.where("patientId").is(Long.valueOf(patientId)));
        query.addCriteria(Criteria.where("isActive").is(true));
        Object o = mongoTemplate.find(query,Disease.class);

        Disease disease = ((ArrayList<Disease>) o).stream().findFirst().orElseThrow(() -> new NoSuchElementInDatasourceException("Patient with such id isn't found"));
        return disease;
    }

    public List<Disease> getDiseasesByPatientId(long patientId){
        Query query = new Query();
        query.addCriteria(Criteria.where("patientId").is(Long.valueOf(patientId)));
        Object o = mongoTemplate.find(query,Disease.class);
        return (ArrayList<Disease>) o;
    }

    public void addTreatmentToDisease(Disease disease, Treatment treatment){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(disease.getId()));

        ArrayList<Treatment> tms = new ArrayList<>();
        tms.add(treatment);
        for(Treatment t : disease.getTreatments()){
            tms.add(t);
        }
        Update update = new Update();
        update.set("treatments", tms.toArray());//treatments);
        mongoTemplate.findAndModify(query, update, Disease.class);
    }

    public void deactivateDiseaseByPatientId(long patientId){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(Long.valueOf(patientId)));
        query.addCriteria(Criteria.where("isActive").is(true));
        Update update = new Update();
        update.set("isActive", false);
        mongoTemplate.findAndModify(query, update, Disease.class);
    }

    public void setWardByPatientId(long patientId, String ward){
        Query query = new Query();
        query.addCriteria(Criteria.where("patientId").is(Long.valueOf(patientId)));
        query.addCriteria(Criteria.where("isActive").is(true));
        Update update = new Update();
        update.set("ward", ward);
        mongoTemplate.findAndModify(query, update, Disease.class);
    }
}

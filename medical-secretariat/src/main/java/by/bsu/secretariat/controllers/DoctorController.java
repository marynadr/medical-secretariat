package by.bsu.secretariat.controllers;

import by.bsu.secretariat.controllers.dto.DiseaseDTO;
import by.bsu.secretariat.controllers.dto.PatientDTO;
import by.bsu.secretariat.controllers.dto.TreatmentDTO;
import by.bsu.secretariat.dao.entity.Disease;
import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.exceptions.InvalidModificationException;
import by.bsu.secretariat.services.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/doctor")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    private Long enteredDoctorId;

    @RequestMapping(value = "/enterDoctorId",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> enterDoctorId(@PathVariable long id){
        if(doctorService.checkDoctorId(id)){
            enteredDoctorId = id;
            return new ResponseEntity<>("", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("ERROR: doctor with such id isn't exist", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/patients/getReadyToVisit",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getReadyToVisitPatients(){
        if(enteredDoctorId == null){
            return new ResponseEntity<>("ERROR: doctor id isn't entered", HttpStatus.FORBIDDEN);
        }
        List<Patient> patients = doctorService.getReadyToVisitPatients(enteredDoctorId);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/patients/acceptPatient",
            method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> acceptPatient(@RequestBody @Valid DiseaseDTO disease, Errors errors) {
        if (errors.hasErrors()) {
            log.error(errors.getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>("Invalid json received: " + errors.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        if(enteredDoctorId == null){
            return new ResponseEntity<>("ERROR: doctor id isn't entered", HttpStatus.FORBIDDEN);
        }
        try {
            doctorService.createDiseaseAndUpdatePatient(disease, enteredDoctorId);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (InvalidModificationException ime) {
            log.error(ime.getMessage());
            return new ResponseEntity<>(ime.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error in creation disease for patient with id ["
                    + String.valueOf(disease.getPatientId())
                    + "]: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/patients/getHospitalized",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getHospitalizedPatients(){
        List<Patient> patients = doctorService.getHospitalizedPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/getAssignedToCurrentDoctor",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getAssignedPatients(){
        if(enteredDoctorId == null){
            return new ResponseEntity<>("ERROR: doctor id isn't entered", HttpStatus.FORBIDDEN);
        }
        List<Patient> patients = doctorService.getAssignedPatients(enteredDoctorId);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/getByPeriod",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getPatientsByPeriod(@RequestParam(name = "startdate") String startdate,
                                          @RequestParam(name = "enddate") String enddate){
        LocalDate start, finish;
        try {
            start = LocalDate.parse(startdate);
            finish = LocalDate.parse(enddate);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("wrong time format", HttpStatus.BAD_REQUEST);
        }

        List<Patient> patients = doctorService.getPatientsByPeriod(start, finish);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/getPatientsByActiveDiagnose",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getPatientsByDiagnose(@RequestParam(name = "diagnose") String diagnose){
        List<Patient> patients = doctorService.getPatientsByActiveDiagnose(diagnose);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/getActiveDiseaseOfPatient",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getActiveDiseaseOfPatient(@RequestParam long patientId){
        Disease patients = doctorService.getActiveDiseaseOfPatient(patientId);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/getAllDiseasesOfPatient",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getAllDiseasesOfPatient(@RequestParam long patientId){
        List<Disease> patients = doctorService.getAllDiseasesOfPatient(patientId);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/patients/addTreatment",
            method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addTreatment(@RequestBody TreatmentDTO treatmentDTO, Errors errors) {
        if (errors.hasErrors()) {
            log.error(errors.getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>("Invalid json received: " + errors.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        if(enteredDoctorId == null){
            return new ResponseEntity<>("ERROR: doctor id isn't entered", HttpStatus.FORBIDDEN);
        }
        try {
            doctorService.addTreatment(treatmentDTO, enteredDoctorId);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (InvalidModificationException ime) {
            log.error(ime.getMessage());
            return new ResponseEntity<>(ime.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error in creation disease for patient with id ["
                    + String.valueOf(treatmentDTO.getPatientId())
                    + "]: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(
            value = "/patients/discharge",
            method = RequestMethod.PATCH)
    public @ResponseBody
    ResponseEntity<?> dischargePatient(@RequestParam long patientId) {
        try {
            doctorService.dischargePatient(patientId);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (InvalidModificationException ime) {
            log.error(ime.getMessage());
            return new ResponseEntity<>(ime.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error in discharging patient with id ["
                    + String.valueOf(patientId)
                    + "]: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

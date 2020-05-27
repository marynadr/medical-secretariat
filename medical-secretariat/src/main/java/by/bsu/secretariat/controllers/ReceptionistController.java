package by.bsu.secretariat.controllers;


import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.entity.User;
import by.bsu.secretariat.exceptions.InvalidModificationException;
import by.bsu.secretariat.services.AdministrationService;
import by.bsu.secretariat.services.RegistrarService;
import by.bsu.secretariat.services.ReceptionistService;
import by.bsu.secretariat.services.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/receptionist")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReceptionistController {


    @Autowired
    private ReceptionistService receptionistService;


    @RequestMapping(value = "/patients/registered",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getRegisteredPatients(){
        List<Patient> patients = receptionistService.getPatientsByStatus("REGISTERED");
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/doctors",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getAllDoctors(){
        List<User> doctors = receptionistService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/haveVisitedDoctor",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getPatientsHaveVisitedDoctor(){
        List<Patient> patients = receptionistService.getPatientsByStatus("DOCTOR_VISITED");
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/patients/sendToDoctor",
            method = RequestMethod.PATCH)
    public @ResponseBody
    ResponseEntity<?> sendPatientToDoctor(@RequestParam long patient_id, @RequestParam long doctor_id) {
        try {
            receptionistService.sendPatientToDoctor(patient_id, doctor_id);
            return new ResponseEntity<>("", HttpStatus.OK);
        }catch (InvalidModificationException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(
            value = "/patients/hospitalize",
            method = RequestMethod.PATCH)
    public @ResponseBody
    ResponseEntity<?> hospitalizePatient(@RequestParam long patient_id, @RequestParam String ward) {
        try {
            receptionistService.hospitalizePatient(patient_id, ward);
            return new ResponseEntity<>("", HttpStatus.OK);
        }catch (InvalidModificationException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


}

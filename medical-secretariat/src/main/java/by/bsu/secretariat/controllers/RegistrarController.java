package by.bsu.secretariat.controllers;

import by.bsu.secretariat.controllers.dto.PatientDTO;
import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.services.RegistrarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.validation.Errors;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/registrar")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegistrarController {

    @Autowired
    private RegistrarService registrarService;


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

        List<Patient> patients = registrarService.getPatientsByPeriod(start, finish);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/{id}",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getPatientsById(@RequestParam long id){

        Patient patient = registrarService.getPatientById(id);

        return new ResponseEntity<>(patient, HttpStatus.OK);
    }


    @RequestMapping(value = "/patients/getAll",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getAllPatients(){
        List<Patient> patients = registrarService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/readyToLeave",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getReadyToLeavePatients(){
        List<Patient> patients = registrarService.getPatientsByStatus("READY_TO_LEAVE");
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/patients/registerNewPatient",
            method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> registerNewPatient(@RequestBody @Valid PatientDTO patient , Errors errors) {
        if (errors.hasErrors()) {
            log.error(errors.getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>("Invalid json received: " + errors.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        try {
            registrarService.registerNewPatient(patient);
            return new ResponseEntity<>("", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error in creation new patient: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(
            value = "/patients/registerExistingPatient",
            method = RequestMethod.PATCH)
    public @ResponseBody
    ResponseEntity<?> registerExistingPatient(@RequestParam long patientId) {
        try {
            registrarService.registerExistingPatient(patientId);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Error in registering existing patient: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(
            value = "/patients/discharge/{id}",
            method = RequestMethod.PATCH)
    public @ResponseBody
    ResponseEntity<?> dischargePatient(@RequestParam long id) {
        try {
            registrarService.dischargePatientById(id);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}

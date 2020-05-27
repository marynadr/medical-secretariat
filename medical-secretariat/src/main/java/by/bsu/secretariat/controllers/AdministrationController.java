package by.bsu.secretariat.controllers;


import by.bsu.secretariat.controllers.dto.PatientReportDTO;
import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.entity.PatientStatus;
import by.bsu.secretariat.services.AdministrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/administration")
public class AdministrationController {
    @Autowired
    private AdministrationService administrationService;

    @RequestMapping(value = "/patients/generateReportByDate",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getPatientsReport(@RequestParam(name = "startdate") String startdate,
                                          @RequestParam(name = "enddate") String enddate){
        LocalDate start, finish;
        try {
            start = LocalDate.parse(startdate);
            finish = LocalDate.parse(enddate);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("wrong time format", HttpStatus.BAD_REQUEST);
        }

        List<PatientReportDTO> patients = administrationService.generatePatientsReport(start, finish);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/generateReportByPatientsStatus",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getPatientsReport(@RequestParam(name = "status") String status){

        List<PatientReportDTO> patients = administrationService.getPatientsByStatus(status);

        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @RequestMapping(value = "/patients/getAllAvailableStatuses",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getPatientsStatuses(){
        return new ResponseEntity<>(PatientStatus.values(), HttpStatus.OK);
    }

    @RequestMapping(value = "/diseases/generateReportByDate",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    ResponseEntity<?> getDiseasesReport(@RequestParam(name = "startdate") String startdate,
                                        @RequestParam(name = "enddate") String enddate){
        LocalDate start, finish;
        try {
            start = LocalDate.parse(startdate);
            finish = LocalDate.parse(enddate);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("wrong time format", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(administrationService.generateDiseasesReport(start, finish), HttpStatus.OK);
    }
}

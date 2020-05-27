package by.bsu.secretariat.controllers;

import by.bsu.secretariat.exceptions.*;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ApiResponse(code = 404, message = "Resource not found")
    @ExceptionHandler({NoSuchElementInDatasourceException.class})
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementInDatasourceException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ApiResponse(code = 400, message = "Bad request")
    @ExceptionHandler({InvalidElementAddingException.class})
    public ResponseEntity<?> handleInvalidElementAddingException(InvalidElementAddingException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ApiResponse(code = 400, message = "Bad request")
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> handleInvalidElementAddingException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ApiResponses(value = {@ApiResponse(code = 500, message = "Server error")})
    @ExceptionHandler({DataSourceException.class})
    public ResponseEntity<?> handleDataSourceException(DataSourceException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiResponses(value = {@ApiResponse(code = 500, message = "Server error")})
    @ExceptionHandler({DataSourceAccessException.class})
    public ResponseEntity<?> handleDataSourceAccessException(DataSourceAccessException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiResponses(value = {@ApiResponse(code = 503, message = "Service failed while initialization")})
    @ExceptionHandler({DataSourceInitializationException.class})
    public ResponseEntity<?> handleDataSourceInitializationException(DataSourceInitializationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR:" + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ApiResponses(value = {@ApiResponse(code = 500, message = "Server error")})
    @ExceptionHandler({TranslateException.class})
    public ResponseEntity<?> handleTranslateExceptionException(TranslateException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiResponses(value = {@ApiResponse(code = 409, message = "Conflict")})
    @ExceptionHandler({InvalidModificationException.class})
    public ResponseEntity<?> handleRuntimeException(InvalidModificationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.CONFLICT);
    }

    @ApiResponses(value = {@ApiResponse(code = 500, message = "Server error")})
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



package pwr.ist.rentalservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pwr.ist.rentalservice.exception.BikeUnavailableException;

import java.util.Map;

@RestControllerAdvice
public class RentalControllerExceptionHandler {
    @ExceptionHandler(BikeUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleBikeUnavailable(BikeUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }
}

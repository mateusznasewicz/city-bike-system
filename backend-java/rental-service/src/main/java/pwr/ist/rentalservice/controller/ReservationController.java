package pwr.ist.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.ist.rentalservice.dto.ApiResponse;
import pwr.ist.rentalservice.dto.ReservationDTO;
import pwr.ist.rentalservice.dto.ReservationRequest;
import pwr.ist.rentalservice.dto.ReservationResponse;
import pwr.ist.rentalservice.service.ReservationService;

@RestController
@RequestMapping("/api/rental/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/active")
    public ResponseEntity<ReservationResponse> getActiveReservation(@RequestHeader ("X-User-Id") String userId){
        ReservationDTO dto = reservationService.getActiveReservation(userId);
        return ResponseEntity.ok(new ReservationResponse(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReservation(@PathVariable("id") String id){
        reservationService.deleteReservation(id);
        return ResponseEntity.ok(new ApiResponse(true));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestHeader ("X-User-Id") String userId, @RequestBody ReservationRequest request){
        ReservationDTO dto = reservationService.createReservation(userId, request);
        return ResponseEntity.ok(new ReservationResponse(dto));
    }
}

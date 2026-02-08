package pwr.ist.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.ist.rentalservice.dto.*;
import pwr.ist.rentalservice.enums.StatusWypozyczenia;
import pwr.ist.rentalservice.service.WypozyczenieService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/rental")
@RequiredArgsConstructor
public class RentalController {

    private final WypozyczenieService wypozyczenieService;

    @GetMapping("/eligibility")
    public ResponseEntity<EligibilityDTO> checkUserEligibility(@RequestHeader("X-User-Id") String userId){
        boolean eligibility = wypozyczenieService.checkUserEligibility(userId);
        EligibilityDTO dto = new EligibilityDTO(eligibility, null);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/start")
    public ResponseEntity<WypozyczenieResponse> startRental(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody StartRentalRequest request
            ){
        WypozyczenieDTO dto = wypozyczenieService.initRental(userId, request);
        return ResponseEntity.accepted().body(new WypozyczenieResponse(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WypozyczenieResponse> checkRental(@PathVariable("id") String rentId){
        WypozyczenieDTO dto = wypozyczenieService.checkRental(rentId);
        return ResponseEntity.ok(new WypozyczenieResponse(dto));
    }

    @GetMapping("/active")
    public ResponseEntity<WypozyczenieResponse> getActiveRental(@RequestHeader("X-User-Id") String userId){
        WypozyczenieDTO dto = wypozyczenieService.getActiveRental(userId);
        return ResponseEntity.ok(new WypozyczenieResponse(dto));
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<WypozyczenieResponse> pauseActiveRental(@PathVariable("id") String rentId){
        WypozyczenieDTO dto = wypozyczenieService.setStateRental(rentId, StatusWypozyczenia.PAUSED);
        return ResponseEntity.ok(new WypozyczenieResponse(dto));
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<WypozyczenieResponse> resumePausedRental(@PathVariable("id") String rentId){
        WypozyczenieDTO dto = wypozyczenieService.setStateRental(rentId, StatusWypozyczenia.ACTIVE);
        return ResponseEntity.ok(new WypozyczenieResponse(dto));
    }

    @PostMapping("/{id}/end")
    public CompletableFuture<ResponseEntity<WypozyczenieResponse>> initEndRental(@PathVariable("id") String rentId){
        CompletableFuture<WypozyczenieResponse> future = new CompletableFuture<>();
        wypozyczenieService.registerFuture(rentId, future);
        wypozyczenieService.initEndRental(rentId);

        return future.thenApply(ResponseEntity::ok)
                .orTimeout(5, TimeUnit.SECONDS);
    }

    @GetMapping("/history")
    public ResponseEntity<RentalHistoryResponse> rentalHistory(@RequestHeader("X-User-Id") String userId){
        List<WypozyczenieDTO> dtos = wypozyczenieService.getRentalHistory(userId);
        return ResponseEntity.ok(new RentalHistoryResponse(dtos));
    }
}

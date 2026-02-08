package pwr.ist.rentalservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.ist.rentalservice.dto.FaultDTO;
import pwr.ist.rentalservice.dto.FaultHistoryResponse;
import pwr.ist.rentalservice.dto.FaultRequest;
import pwr.ist.rentalservice.dto.FaultResponse;
import pwr.ist.rentalservice.service.FaultService;

import java.util.List;

@RestController
@RequestMapping("/api/fault")
@RequiredArgsConstructor
public class FaultController {

    private final FaultService faultService;

    @GetMapping("/history")
    public ResponseEntity<FaultHistoryResponse> getFaultHistory(@RequestHeader("X-User-Id") String userId){
        List<FaultDTO> dtos = faultService.getUserFaultHistory(userId);
        return ResponseEntity.ok(new FaultHistoryResponse(dtos));
    }

    @PostMapping("/report")
    public ResponseEntity<FaultResponse> reportFault(
            @RequestBody FaultRequest request,
            @RequestHeader("X-User-Id") String userId
            ){

        FaultDTO dto = faultService.reportFault(request, userId);
        return ResponseEntity.ok(new FaultResponse(dto));
    }

}

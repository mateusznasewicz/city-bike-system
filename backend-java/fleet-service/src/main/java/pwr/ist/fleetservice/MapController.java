package pwr.ist.fleetservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pwr.ist.fleetservice.dto.BikesResponse;
import pwr.ist.fleetservice.dto.StationsResponse;
import pwr.ist.fleetservice.repository.BikeRepository;
import pwr.ist.fleetservice.repository.StationRepository;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final BikeRepository bikeRepository;
    private final StationRepository stationRepository;

    @GetMapping("/bikes")
    public ResponseEntity<BikesResponse> getBikesMap(){
        return ResponseEntity.ok(
                new BikesResponse( bikeRepository.findAll() )
        );
    }

    @GetMapping("/stations")
    public ResponseEntity<StationsResponse> getStationsMap(){
        return ResponseEntity.ok(
                new StationsResponse( stationRepository.findAll() )
        );
    }
}

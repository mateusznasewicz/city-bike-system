package pwr.ist.fleetservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.ist.fleetservice.entity.Station;

public interface StationRepository extends JpaRepository<Station, String> {
}

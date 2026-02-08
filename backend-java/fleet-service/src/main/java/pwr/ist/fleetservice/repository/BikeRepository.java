package pwr.ist.fleetservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.ist.fleetservice.entity.Bike;

public interface BikeRepository extends JpaRepository<Bike, String> {
}

package pwr.ist.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwr.ist.rentalservice.entity.Rower;

import java.util.Optional;

@Repository
public interface BikeRepository extends JpaRepository<Rower, String> {
    Optional<Rower> findByKodQr(String kod_qr);
}

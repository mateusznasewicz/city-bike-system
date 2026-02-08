package pwr.ist.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pwr.ist.rentalservice.entity.Reservation;
import pwr.ist.rentalservice.enums.ReservationStatus;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    @Query("SELECT r FROM Reservation r WHERE r.uzytkownik.id_uzytkownika = :userId AND r.status = :status")
    Optional<Reservation> findByUserIdAndStatus(
            @Param("userId") String userId,
            @Param("status") ReservationStatus status
    );
}

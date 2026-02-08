package pwr.ist.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pwr.ist.rentalservice.entity.Fault;

import java.util.List;

public interface FaultRepository extends JpaRepository<Fault, String> {
    @Query("SELECT f FROM Fault f WHERE f.uzytkownik.id_uzytkownika = :userId")
    List<Fault> findAllFaultByUser(@Param("userId") String userId);
}

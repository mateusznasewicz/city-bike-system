package pwr.ist.rentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pwr.ist.rentalservice.entity.Wypozyczenie;
import pwr.ist.rentalservice.enums.StatusWypozyczenia;


import java.util.List;
import java.util.Optional;

@Repository
public interface WypozyczenieRepository extends JpaRepository<Wypozyczenie, String> {
    @Query(value = """
    SELECT EXISTS (
        SELECT 1 
        FROM wypozyczenie 
        WHERE user_id = :userId 
        AND status != 'FINISHED'
    )
    """, nativeQuery = true)
    boolean hasActiveRental(@Param("userId") String userId);

    @Query("SELECT w FROM Wypozyczenie w WHERE w.uzytkownik.id_uzytkownika = :userId AND w.status = :status")
    List<Wypozyczenie> findByUserIdAndStatus(
            @Param("userId") String userId,
            @Param("status") StatusWypozyczenia status
    );

    @Query("SELECT w FROM Wypozyczenie w WHERE w.uzytkownik.id_uzytkownika = :userId AND w.status IN ('ACTIVE', 'PAUSED')")
    Optional<Wypozyczenie> findActiveRental(@Param("userId") String userId);
}

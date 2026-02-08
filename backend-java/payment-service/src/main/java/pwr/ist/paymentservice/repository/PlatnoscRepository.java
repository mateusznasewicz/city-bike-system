package pwr.ist.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwr.ist.paymentservice.entity.Platnosc;

import java.util.List;

@Repository
public interface PlatnoscRepository extends JpaRepository<Platnosc, Long> {
    List<Platnosc> findByWalletUzytkownikId(String id);
}

package pwr.ist.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pwr.ist.rentalservice.dto.ReservationDTO;
import pwr.ist.rentalservice.dto.ReservationRequest;
import pwr.ist.rentalservice.entity.Reservation;
import pwr.ist.rentalservice.entity.Rower;
import pwr.ist.rentalservice.entity.Uzytkownik;
import pwr.ist.rentalservice.enums.ReservationStatus;
import pwr.ist.rentalservice.mapper.ReservationMapper;
import pwr.ist.rentalservice.repository.BikeRepository;
import pwr.ist.rentalservice.repository.ReservationRepository;
import pwr.ist.rentalservice.repository.UzytkownikRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    private final UzytkownikRepository uzytkownikRepository;
    private final BikeRepository bikeRepository;

    public ReservationDTO getActiveReservation(String userId) {
        Reservation reservation = reservationRepository.findByUserIdAndStatus(userId, ReservationStatus.ACTIVE)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Active reservation for user: " + userId + " not found"
                ));

        return reservationMapper.toDTO(reservation);
    }

    public void deleteReservation(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + id));
        reservationRepository.delete(reservation);
    }

    public ReservationDTO createReservation(String userId, ReservationRequest request) {
        Rower rower = bikeRepository.findById(request.bikeId()).orElseThrow(() -> new RuntimeException("bike not found: "+ request.bikeId()));
        Uzytkownik uzytkownik = uzytkownikRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found: "+ userId));

        Reservation reservation = Reservation.builder()
                .rower(rower)
                .uzytkownik(uzytkownik)
                .data_utworzenia(LocalDateTime.now().toString())
                .data_wygasniecia(LocalDateTime.now().plusMinutes(15).toString())
                .status(ReservationStatus.ACTIVE)
                .build();

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDTO(saved);
    }
}

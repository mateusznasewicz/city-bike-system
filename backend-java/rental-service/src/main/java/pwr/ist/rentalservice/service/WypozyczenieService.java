package pwr.ist.rentalservice.service;

import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pwr.ist.rentalservice.dto.StartRentalRequest;
import pwr.ist.rentalservice.dto.WypozyczenieDTO;
import pwr.ist.rentalservice.dto.WypozyczenieResponse;
import pwr.ist.rentalservice.entity.Reservation;
import pwr.ist.rentalservice.entity.Rower;
import pwr.ist.rentalservice.entity.Uzytkownik;
import pwr.ist.rentalservice.entity.Wypozyczenie;
import pwr.ist.rentalservice.enums.ReservationStatus;
import pwr.ist.rentalservice.enums.StatusRoweru;
import pwr.ist.rentalservice.enums.StatusWypozyczenia;
import pwr.ist.rentalservice.events.RentalPaymentCommand;
import pwr.ist.rentalservice.exception.BikeUnavailableException;
import pwr.ist.rentalservice.mapper.WypozyczenieMapper;
import pwr.ist.rentalservice.repository.BikeRepository;
import pwr.ist.rentalservice.repository.ReservationRepository;
import pwr.ist.rentalservice.repository.UzytkownikRepository;
import pwr.ist.rentalservice.repository.WypozyczenieRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class WypozyczenieService {

    private final WypozyczenieRepository wypozyczenieRepository;
    private final RedisService redisService;
    private final KafkaProducer producer;
    private final WypozyczenieMapper wypozyczenieMapper;
    private final BikeRepository bikeRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final ReservationRepository reservationRepository;
    private final Map<String, CompletableFuture<WypozyczenieResponse>> futures = new ConcurrentHashMap<>();

    public boolean checkUserEligibility(String userId){
        return !wypozyczenieRepository.hasActiveRental(userId);
    }

    private Rower getBike(String bikeId){
        Rower bike;
        if(bikeId.contains("BIKE-")){
            bike = bikeRepository.findByKodQr(bikeId).orElseThrow(() -> new RuntimeException("QR bike not found: "+ bikeId));
        }else{
            bike = bikeRepository.findById(bikeId).orElseThrow(() -> new RuntimeException("MANUAL bike not found: "+ bikeId));
        }
        return bike;
    }

    public WypozyczenieDTO initRental(String userId, StartRentalRequest request){
        Rower rower = this.getBike(request.bikeId());
        StatusRoweru status = rower.getStatusRoweru();
        if (status == StatusRoweru.RENTED) {
            throw new BikeUnavailableException("Rower jest obecnie niedostępny. Status: " + status);
        }

        Uzytkownik uzytkownik = uzytkownikRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found: "+ userId));
        rower.setStatusRoweru(StatusRoweru.RENTED);
        bikeRepository.save(rower);
        Wypozyczenie wypozyczenie = Wypozyczenie.builder()
                .uzytkownik(uzytkownik)
                .rower(rower)
                .data_rozpoczecia(LocalDateTime.now().toString())
                .czas_pauzy_sekundy(0)
                .metoda_uruchomienia("QR")
                .status(StatusWypozyczenia.ACTIVE)
                .build();
        wypozyczenieRepository.save(wypozyczenie);


        producer.sendRentalInit(wypozyczenie);
        return wypozyczenieMapper.toDTO(wypozyczenie);
    }

    public WypozyczenieDTO checkRental(String rentId){
        Wypozyczenie wypozyczenie = wypozyczenieRepository.findById(rentId)
                .orElseThrow(() -> new RuntimeException("rental not found: "+rentId));

        return wypozyczenieMapper.toDTO(wypozyczenie);
    }

    public void startRental(String wypozyczenieId, String bikeId){
        log.info("WYPOZYCZENIE UDANE");
    }

    public WypozyczenieDTO getActiveRental(String userId) {
        Wypozyczenie wypozyczenie = wypozyczenieRepository.findActiveRental(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"active rental not found: "+userId));

        return wypozyczenieMapper.toDTO(wypozyczenie);
    }

    public WypozyczenieDTO setStateRental(String rentId, StatusWypozyczenia state) {
        Wypozyczenie wypozyczenie = wypozyczenieRepository.findById(rentId)
                .orElseThrow(() -> new RuntimeException("rental not found: "+rentId));

        wypozyczenie.setStatus(state);
        Wypozyczenie saved = wypozyczenieRepository.save(wypozyczenie);
        return wypozyczenieMapper.toDTO(saved);
    }

    public WypozyczenieDTO initEndRental(String rentId) {
        WypozyczenieDTO dto = this.setStateRental(rentId, StatusWypozyczenia.FINISHED);
        String userid = dto.id_uzytkownika();
        Optional<Reservation> reservation = reservationRepository.findByUserIdAndStatus(userid, ReservationStatus.ACTIVE);
        if (reservation.isPresent()) {
            reservation.get().setStatus(ReservationStatus.INACTIVE);
            reservationRepository.save(reservation.get());
        }
        producer.sendProcessPayment(new RentalPaymentCommand(dto.id_uzytkownika(), dto.id_roweru(), dto.id_wypozyczenia(), dto.data_rozpoczecia()));
        return dto;
    }

    public void endRental(String wypozyczenieId, String bikeId) {
        Wypozyczenie wypozyczenie = wypozyczenieRepository.findById(wypozyczenieId)
                .orElseThrow(() -> new RuntimeException("rental not found: "+wypozyczenieId));

        Rower rower = this.getBike(bikeId);

        wypozyczenie.setStatus(StatusWypozyczenia.FINISHED);
        wypozyczenie.setData_zakonczenia(LocalDateTime.now().toString());
        rower.setStatusRoweru(StatusRoweru.AVAILABLE);
        redisService.saveBikeStatus(bikeId, StatusRoweru.AVAILABLE);

        wypozyczenieRepository.save(wypozyczenie);
        bikeRepository.save(rower);

        WypozyczenieDTO dto = wypozyczenieMapper.toDTO(wypozyczenie);

        CompletableFuture<WypozyczenieResponse> future = futures.get(wypozyczenieId);
        if (future != null) {
            future.complete(new WypozyczenieResponse(dto));
        }
    }

    public List<WypozyczenieDTO> getRentalHistory(String userId) {

        return wypozyczenieRepository.findByUserIdAndStatus(userId, StatusWypozyczenia.FINISHED)
                .stream().map(wypozyczenieMapper::toDTO).toList();
    }

    public void setWypozyczenieKoszt(String wypozyczenieId, double koszt) {
        Wypozyczenie wypozyczenie = wypozyczenieRepository.findById(wypozyczenieId)
                .orElseThrow(() -> new RuntimeException("rental not found: "+wypozyczenieId));
        log.info("[RENTAL] ustawianie kosztu: "+koszt);
        wypozyczenie.setKoszt_calkowity(koszt);
        wypozyczenieRepository.save(wypozyczenie);
    }

    public void registerFuture(String rentId, CompletableFuture<WypozyczenieResponse> future) {
        futures.put(rentId, future);
    }
}

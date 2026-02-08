package pwr.ist.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import pwr.ist.rentalservice.dto.FaultDTO;
import pwr.ist.rentalservice.dto.FaultRequest;
import pwr.ist.rentalservice.entity.Fault;
import pwr.ist.rentalservice.entity.Rower;
import pwr.ist.rentalservice.entity.Uzytkownik;
import pwr.ist.rentalservice.entity.Wypozyczenie;
import pwr.ist.rentalservice.mapper.FaultMapper;
import pwr.ist.rentalservice.repository.BikeRepository;
import pwr.ist.rentalservice.repository.FaultRepository;
import pwr.ist.rentalservice.repository.UzytkownikRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaultService {

    private final FaultRepository faultRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final BikeRepository bikeRepository;

    private final FaultMapper faultMapper;

    public List<FaultDTO> getUserFaultHistory(String userId) {
        return faultRepository.findAllFaultByUser(userId)
                .stream().map(faultMapper::toDTO).toList();
    }

    public FaultDTO reportFault(FaultRequest request, String userId) {
        Uzytkownik uzytkownik = uzytkownikRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found: "+userId));

        Rower rower = bikeRepository.findById(request.bikeId())
                .orElseThrow(() -> new RuntimeException("bike not found: "+ request.bikeId()));

        Fault fault = Fault.builder()
                .rower(rower)
                .uzytkownik(uzytkownik)
                .typ_usterki(request.type())
                .opis(request.description())
                .data_zgloszenia(LocalDateTime.now().toString())
                .czy_potwierdzone(false)
                .czy_zweryfikowane(false)
                .build();

        Fault savedFault = faultRepository.save(fault);
        return faultMapper.toDTO(savedFault);
    }
}

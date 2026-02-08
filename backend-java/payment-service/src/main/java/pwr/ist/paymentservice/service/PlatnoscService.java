package pwr.ist.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pwr.ist.paymentservice.dto.PlatnoscDTO;
import pwr.ist.paymentservice.entity.Platnosc;
import pwr.ist.paymentservice.entity.Wallet;
import pwr.ist.paymentservice.enums.TypPlatnosci;
import pwr.ist.paymentservice.mapper.PlatnoscMapper;
import pwr.ist.paymentservice.repository.PlatnoscRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatnoscService {

    private final PlatnoscRepository platnoscRepository;
    private final PlatnoscMapper platnoscMapper;

    public List<PlatnoscDTO> getTransactionsByUserId(String userId){
        List<Platnosc> transakcje = platnoscRepository.findByWalletUzytkownikId(userId);

        return transakcje.stream()
                .map(platnoscMapper::toDTO)
                .toList();
    }

    public PlatnoscDTO save(Wallet wallet, String opis, double kwota, TypPlatnosci typ) {

        Platnosc platnosc = Platnosc.builder()
                .kwota(kwota)
                .opis(opis)
                .typ_platnosci(typ)
                .czas_zlecenia(LocalDateTime.now().toString())
                .wallet(wallet)
                .build();

        Platnosc savedPlatnosc = platnoscRepository.save(platnosc);
        return platnoscMapper.toDTO(savedPlatnosc);
    }
}

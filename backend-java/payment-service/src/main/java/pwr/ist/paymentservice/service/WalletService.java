package pwr.ist.paymentservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pwr.ist.paymentservice.dto.*;
import pwr.ist.paymentservice.entity.PaymentMethod;
import pwr.ist.paymentservice.entity.Platnosc;
import pwr.ist.paymentservice.entity.Wallet;
import pwr.ist.paymentservice.enums.TypPlatnosci;
import pwr.ist.paymentservice.mapper.PaymentMethodMapper;
import pwr.ist.paymentservice.repository.PaymentMethodRepository;
import pwr.ist.paymentservice.repository.WalletRepository;
import pwr.ist.paymentservice.mapper.WalletMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final PlatnoscService platnoscService;
    private final WalletMapper walletMapper;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;


    public WalletDTO getWalletByUserId(String userId){
        return walletRepository.findByUzytkownikId(userId)
                .map(walletMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for id: " + userId));
    }

    private double updateBalance(Wallet wallet, double kwota){
        double saldoPoTranksakcji = wallet.getSaldo() + kwota;
        wallet.setSaldo(saldoPoTranksakcji);
        walletRepository.save(wallet);

        return saldoPoTranksakcji;
    }

    public TopUpResponse topUp(String userId, TopUpRequest request) {
        Wallet wallet = walletRepository.findByUzytkownikId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for userid " + userId));
        String opis = "Doladowanie konta: " + request.method();
        PlatnoscDTO platnoscDTO = platnoscService.save(wallet, opis, request.amount(), TypPlatnosci.TOP_UP);
        double saldoPoTransakcji = this.updateBalance(wallet, request.amount());

        return new TopUpResponse(true, saldoPoTransakcji, platnoscDTO);
    }

    public List<PaymentMethodDTO> getPaymentMethod(String userId) {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAllByWalletUzytkownikId(userId);
        return paymentMethods.stream().map(paymentMethodMapper::toDTO).toList();
    }

    @Transactional
    public PaymentMethodDTO addPaymentMethod(String userId, PaymentMethodDTO request) {
        Wallet wallet = walletRepository.findByUzytkownikId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for userid " + userId));

        PaymentMethod method = PaymentMethod.builder()
                .typ(request.typ())
                .marka_karty(request.marka_karty())
                .ostatnie_cztery(request.ostatnie_cztery())
                .wallet(wallet)
                .build();

        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(method);
        return paymentMethodMapper.toDTO(savedPaymentMethod);
    }

    public boolean blockFunds(String userId){
        Wallet wallet = walletRepository.findByUzytkownikId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for userid " + userId));
        double saldo = wallet.getSaldo();
        wallet.setSaldo(saldo - 20);
        wallet.setZablokowaneSrodki(20);
        walletRepository.save(wallet);
        return true;
    }

    public double withdrawFunds(String userId, String dataRozpoczecia) {
        Wallet wallet = walletRepository.findByUzytkownikId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for userid " + userId));

        LocalDateTime rozpoczecie = LocalDateTime.parse(dataRozpoczecia);
        LocalDateTime zakonczenie = LocalDateTime.now();
        Duration trwanie = Duration.between(rozpoczecie, zakonczenie);
        long minuty = trwanie.toMinutes();

        double koszt = 0.5 * minuty;
        log.info("[PAYMENT] calkowity koszt: "+koszt);
        double zablokowane = wallet.getZablokowaneSrodki() - koszt;

        double saldo = wallet.getSaldo() + zablokowane;
        wallet.setZablokowaneSrodki(0);
        wallet.setSaldo(saldo);

        platnoscService.save(wallet, "Oplata za przejazd", -koszt, TypPlatnosci.FEE);
        walletRepository.save(wallet);
        return koszt;
    }
}

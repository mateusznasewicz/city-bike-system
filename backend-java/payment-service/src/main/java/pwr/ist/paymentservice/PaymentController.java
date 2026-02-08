package pwr.ist.paymentservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.ist.paymentservice.dto.*;
import pwr.ist.paymentservice.service.PlatnoscService;
import pwr.ist.paymentservice.service.WalletService;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class PaymentController {

    private final WalletService walletService;
    private final PlatnoscService platnoscService;

    @GetMapping("/")
    public ResponseEntity<WalletDTO> getWallet(@RequestHeader("X-User-Id") String userId){
        WalletDTO walletDTO = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(walletDTO);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<PlatnoscDTO>> getTransaction(@RequestHeader("X-User-Id") String userId){
        List<PlatnoscDTO> transactions = platnoscService.getTransactionsByUserId(userId);

        if(transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/topup")
    public ResponseEntity<TopUpResponse> topUp(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody TopUpRequest request
            ){
        return ResponseEntity.ok(walletService.topUp(userId, request));
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<PaymentMethodsResponse> getPaymentMethod(@RequestHeader("X-User-Id") String userId){
        List<PaymentMethodDTO> dtos = walletService.getPaymentMethod(userId);
        return ResponseEntity.ok(new PaymentMethodsResponse(dtos));
    }

    @PostMapping("/payment-methods")
    public ResponseEntity<PaymentMethodResponse> addPaymentMethod(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody PaymentMethodDTO request
    ){
        PaymentMethodDTO dto = walletService.addPaymentMethod(userId, request);
        return ResponseEntity.ok(new PaymentMethodResponse(true, dto));
    }
}

package pwr.ist.paymentservice.dto;

import pwr.ist.paymentservice.enums.WalletStatus;

public record WalletDTO(
        double saldo,
        WalletStatus status,
        boolean ma_podpieta_karte
) {
}

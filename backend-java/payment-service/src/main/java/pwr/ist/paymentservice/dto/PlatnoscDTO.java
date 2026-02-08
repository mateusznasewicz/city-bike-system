package pwr.ist.paymentservice.dto;

import pwr.ist.paymentservice.enums.TypPlatnosci;

public record PlatnoscDTO(
        String id_transakcji,
        double kwota,
        TypPlatnosci typ,
        String czas_rejestracji,
        String opis
) {
}

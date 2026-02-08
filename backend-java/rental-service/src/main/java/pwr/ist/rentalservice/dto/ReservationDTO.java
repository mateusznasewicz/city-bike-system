package pwr.ist.rentalservice.dto;

import pwr.ist.rentalservice.enums.ReservationStatus;

public record ReservationDTO(
        String id_rezerwacji,
        String id_uzytkownika,
        String id_roweru,
        ReservationStatus status,
        String data_utworzenia,
        String data_wygasniecia
) {
}

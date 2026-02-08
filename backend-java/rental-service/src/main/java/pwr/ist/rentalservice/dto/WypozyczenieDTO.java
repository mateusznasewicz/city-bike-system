package pwr.ist.rentalservice.dto;

public record WypozyczenieDTO(
        String id_wypozyczenia,
        String status,
        String data_rozpoczecia,
        String data_zakonczenia,
        String id_roweru,
        String id_uzytkownika,
        double koszt_calkowity
) {
}

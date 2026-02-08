package pwr.ist.rentalservice.dto;

public record FaultDTO(
        String id_zgloszenia,
        String id_roweru,
        String id_uzytkownika,
        String typ_usterki,
        String opis,
        String data_zgloszenia,
        boolean czy_zweryfikowane,
        boolean czy_potwierdzone,
        String data_weryfikacji,
        Double kwata_nagrody
) {
}

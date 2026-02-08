package pwr.ist.rentalservice.entity;

import jakarta.persistence.*;
import lombok.*;
import pwr.ist.rentalservice.enums.StatusWypozyczenia;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Wypozyczenie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id_wypozyczenia;

    @Enumerated(EnumType.STRING)
    private StatusWypozyczenia status;

    private String data_rozpoczecia;

    private String data_zakonczenia;

    private double koszt_calkowity;

    private String metoda_uruchomienia;

    private int czas_pauzy_sekundy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Uzytkownik uzytkownik;

    @ManyToOne
    @JoinColumn(name = "bike_id")
    private Rower rower;
}

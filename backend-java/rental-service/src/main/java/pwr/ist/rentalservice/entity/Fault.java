package pwr.ist.rentalservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Fault {

    @Id
    @GeneratedValue
    private String id_zgloszenia;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Uzytkownik uzytkownik;

    @ManyToOne
    @JoinColumn(name = "bike_id")
    private Rower rower;

    private String typ_usterki;

    private String opis;

    private String data_zgloszenia;

    private boolean czy_zweryfikowane;

    private boolean czy_potwierdzone;

    private String data_weryfikacji;

    private Double kwata_nagrody;
}

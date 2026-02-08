package pwr.ist.rentalservice.entity;

import jakarta.persistence.*;
import jdk.jfr.Label;
import lombok.*;
import pwr.ist.rentalservice.enums.ReservationStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id_rezerwacji;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Uzytkownik uzytkownik;

    @ManyToOne
    @JoinColumn(name = "bike_id")
    private Rower rower;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private String data_utworzenia;

    private String data_wygasniecia;
}

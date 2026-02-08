package pwr.ist.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;
import pwr.ist.paymentservice.enums.TypPlatnosci;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Platnosc {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private TypPlatnosci typ_platnosci;

    private double kwota;

    private String czas_zlecenia;

    private String opis;
}

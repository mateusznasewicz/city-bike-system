package pwr.ist.fleetservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pwr.ist.fleetservice.enums.StatusRoweru;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Bike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id_roweru;

    private String kod_qr;

    @Enumerated(EnumType.STRING)
    private StatusRoweru status;

    private int poziom_baterii;

    private double lokalizacja_szerokosc;

    private double lokalizacja_dlugosc;

    private int zasieg_km;
}

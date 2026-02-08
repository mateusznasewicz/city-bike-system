package pwr.ist.fleetservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id_stacji;

    private String nazwa;

    private double lokalizacja_szerokosc;

    private double lokalizacja_dlugosc;

    private int pojemnosc;

    private int dostepne_rowery;

    private int dostepne_stojaki;
}

package pwr.ist.rentalservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.ist.rentalservice.enums.StatusRoweru;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rower {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id_roweru;

    @OneToMany(mappedBy = "rower")
    private List<Wypozyczenie> wypozyczenia;

    @Enumerated(EnumType.STRING)
    private StatusRoweru statusRoweru;

    private String kodQr;
}

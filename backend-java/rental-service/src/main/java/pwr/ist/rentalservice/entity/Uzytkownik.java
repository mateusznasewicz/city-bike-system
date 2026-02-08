package pwr.ist.rentalservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Uzytkownik {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id_uzytkownika;

    @OneToMany(mappedBy = "uzytkownik")
    private List<Wypozyczenie> wypozyczenia;
}

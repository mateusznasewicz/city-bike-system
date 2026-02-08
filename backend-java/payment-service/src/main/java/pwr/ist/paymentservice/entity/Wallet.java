package pwr.ist.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.ist.paymentservice.enums.WalletStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Uzytkownik uzytkownik;

    private double saldo;

    private double zablokowaneSrodki;

    @Enumerated(EnumType.STRING)
    private WalletStatus status;

    @OneToMany(mappedBy = "wallet")
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @OneToMany(mappedBy = "wallet")
    private List<Platnosc> platnosci = new ArrayList<>();
}

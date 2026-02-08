package pwr.ist.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id_metody;

    @Builder.Default
    private String typ = "CARD";

    private String ostatnie_cztery;

    private String marka_karty;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}

package pwr.ist.paymentservice.dto;

public record PaymentMethodDTO(
        String typ,
        String ostatnie_cztery,
        String marka_karty
) {
}

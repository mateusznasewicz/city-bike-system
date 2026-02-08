package pwr.ist.paymentservice.dto;

public record PaymentMethodResponse(
        boolean success,
        PaymentMethodDTO method
) {
}

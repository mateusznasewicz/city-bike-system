package pwr.ist.paymentservice.dto;

public record TopUpRequest(
        double amount,
        String method
) {
}

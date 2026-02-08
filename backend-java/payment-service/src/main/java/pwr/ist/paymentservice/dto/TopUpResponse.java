package pwr.ist.paymentservice.dto;

public record TopUpResponse(
        boolean success,
        double newBalance,
        PlatnoscDTO transaction
) {
}

package pwr.ist.rentalservice.dto;

public record FaultRequest(
        String bikeId,
        String type,
        String description
) {
}

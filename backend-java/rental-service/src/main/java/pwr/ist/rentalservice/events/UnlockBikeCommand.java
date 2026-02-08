package pwr.ist.rentalservice.events;

public record UnlockBikeCommand(String userId, String bikeId, String wypozyczenieId) {
}

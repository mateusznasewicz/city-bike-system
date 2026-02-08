package pwr.ist.rentalservice.events;

public record LockBikeCommand(String userId, String bikeId, String wypozyczenieId) {
}

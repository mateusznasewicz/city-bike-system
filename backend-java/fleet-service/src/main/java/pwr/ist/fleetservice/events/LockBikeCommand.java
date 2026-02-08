package pwr.ist.fleetservice.events;

public record LockBikeCommand(String userId, String bikeId, String wypozyczenieId) {
}

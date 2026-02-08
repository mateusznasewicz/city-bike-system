package pwr.ist.fleetservice.events;

public record UnlockBikeCommand(String userId, String bikeId, String wypozyczenieId) {
}


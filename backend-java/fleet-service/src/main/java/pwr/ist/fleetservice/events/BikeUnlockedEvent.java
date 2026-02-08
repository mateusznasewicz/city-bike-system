package pwr.ist.fleetservice.events;

public record BikeUnlockedEvent(String userId, String bikeId, String wypozyczenieId, boolean unlocked) {
}

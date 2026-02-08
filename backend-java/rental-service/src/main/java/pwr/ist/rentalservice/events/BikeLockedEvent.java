package pwr.ist.rentalservice.events;

public record BikeLockedEvent(String userId, String bikeId, String wypozyczenieId, boolean unlocked) {
}

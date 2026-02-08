package pwr.ist.rentalservice.events;

public record BikeUnlockedEvent(String userId, String bikeId, String wypozyczenieId, boolean unlocked) {
}

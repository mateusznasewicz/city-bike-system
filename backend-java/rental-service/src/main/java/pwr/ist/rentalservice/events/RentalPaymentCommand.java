package pwr.ist.rentalservice.events;

public record RentalPaymentCommand(String userId, String bikeId, String wypozyczenieId, String data_rozpoczecia) {
}

package pwr.ist.rentalservice.events;

public record PaymentCompletedEvent(RentalPaymentCommand paymentCommand, double koszt) {
}

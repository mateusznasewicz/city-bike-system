package pwr.ist.paymentservice.events;

public record PaymentCompletedEvent(RentalPaymentCommand paymentCommand, double koszt) {
}


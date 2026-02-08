package pwr.ist.paymentservice.events;

public record FundsReservedEvent(RentalInitEvent rentalInit, boolean status) {
}

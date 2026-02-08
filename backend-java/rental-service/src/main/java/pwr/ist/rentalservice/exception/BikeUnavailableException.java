package pwr.ist.rentalservice.exception;

public class BikeUnavailableException extends RuntimeException {
    public BikeUnavailableException(String message){
        super(message);
    }
}

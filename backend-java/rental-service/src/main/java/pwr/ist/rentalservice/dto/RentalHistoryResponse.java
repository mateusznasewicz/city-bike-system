package pwr.ist.rentalservice.dto;

import java.util.List;

public record RentalHistoryResponse(List<WypozyczenieDTO> rentals) {
}

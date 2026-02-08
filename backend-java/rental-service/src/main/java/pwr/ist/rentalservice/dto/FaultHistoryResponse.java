package pwr.ist.rentalservice.dto;

import java.util.List;

public record FaultHistoryResponse(List<FaultDTO> faults) {
}

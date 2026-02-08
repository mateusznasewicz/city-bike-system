package pwr.ist.paymentservice.dto;

import java.util.List;

public record PaymentMethodsResponse(
        List<PaymentMethodDTO> methods
) {
}

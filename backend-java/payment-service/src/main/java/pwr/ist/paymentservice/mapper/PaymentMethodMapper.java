package pwr.ist.paymentservice.mapper;

import org.mapstruct.Mapper;
import pwr.ist.paymentservice.dto.PaymentMethodDTO;
import pwr.ist.paymentservice.entity.PaymentMethod;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodDTO toDTO(PaymentMethod paymentMethod);
}

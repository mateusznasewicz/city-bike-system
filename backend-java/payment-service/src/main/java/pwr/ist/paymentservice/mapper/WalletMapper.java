package pwr.ist.paymentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pwr.ist.paymentservice.dto.WalletDTO;
import pwr.ist.paymentservice.entity.Wallet;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    @Mapping(target = "ma_podpieta_karte", source = "paymentMethods", qualifiedByName = "checkPaymentMethods")
    WalletDTO toDto(Wallet wallet);

    @Named("checkPaymentMethods")
    default boolean hasPaymentMethods(List<?> paymentMethods) {
        return paymentMethods != null && !paymentMethods.isEmpty();
    }
}

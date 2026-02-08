package pwr.ist.paymentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pwr.ist.paymentservice.dto.PlatnoscDTO;
import pwr.ist.paymentservice.entity.Platnosc;

@Mapper(componentModel = "spring")
public interface PlatnoscMapper {

    @Mapping(target = "id_transakcji", source = "id")
    @Mapping(target = "czas_rejestracji", source = "czas_zlecenia")
    @Mapping(target = "typ", source = "typ_platnosci")
    PlatnoscDTO toDTO(Platnosc platnosc);
}

package pwr.ist.rentalservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pwr.ist.rentalservice.dto.WypozyczenieDTO;
import pwr.ist.rentalservice.entity.Wypozyczenie;

@Mapper(componentModel = "spring")
public interface WypozyczenieMapper {

    @Mapping(source = "uzytkownik.id_uzytkownika", target = "id_uzytkownika")
    @Mapping(source = "rower.id_roweru", target = "id_roweru")
    WypozyczenieDTO toDTO(Wypozyczenie wypozyczenie);
}

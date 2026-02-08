package pwr.ist.rentalservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pwr.ist.rentalservice.dto.FaultDTO;
import pwr.ist.rentalservice.entity.Fault;

@Mapper(componentModel = "spring")
public interface FaultMapper {
    @Mapping(source = "uzytkownik.id_uzytkownika", target = "id_uzytkownika")
    @Mapping(source = "rower.id_roweru", target = "id_roweru")
    FaultDTO toDTO(Fault fault);
}

package pwr.ist.rentalservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pwr.ist.rentalservice.dto.ReservationDTO;
import pwr.ist.rentalservice.entity.Reservation;


@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(source = "uzytkownik.id_uzytkownika", target = "id_uzytkownika")
    @Mapping(source = "rower.id_roweru", target = "id_roweru")
    ReservationDTO toDTO(Reservation reservation);
}

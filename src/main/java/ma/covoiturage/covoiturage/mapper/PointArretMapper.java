package ma.covoiturage.covoiturage.mapper;

import ma.covoiturage.covoiturage.dto.request.PointArretCreateRequest;
import ma.covoiturage.covoiturage.dto.response.PointArretResponse;
import ma.covoiturage.covoiturage.entity.PointArret;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PointArretMapper {
    @Mapping(target = "id" ,ignore = true)
    @Mapping(target = "trajet", ignore = true)
    @Mapping(target = "reservationsMontee", ignore = true)
    @Mapping(target = "reservationsDescente", ignore = true)
    PointArret toEntity(PointArretCreateRequest request);

    PointArretResponse toResponse(PointArret pointArret);
}

package ma.covoiturage.covoiturage.mapper;

import ma.covoiturage.covoiturage.dto.request.TrajetCreateRequest;
import ma.covoiturage.covoiturage.dto.response.TrajetResponse;
import ma.covoiturage.covoiturage.entity.Trajet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = PointArretMapper.class)
public interface TrajetMapper {

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "statut" , ignore = true)
    @Mapping(target = "conducteur", ignore = true)
    @Mapping(target = "vehicule", ignore = true)
    @Mapping(target = "pointArrets", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    Trajet toEntity(TrajetCreateRequest request);

    @Mapping(target = "conducteurId", source = "conducteur.id")
    @Mapping(target = "vehiculeId", source = "vehicule.id")
    TrajetResponse toResponse(Trajet trajet);
}

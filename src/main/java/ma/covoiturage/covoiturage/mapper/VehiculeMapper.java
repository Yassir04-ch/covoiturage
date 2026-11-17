package ma.covoiturage.covoiturage.mapper;

import ma.covoiturage.covoiturage.dto.request.VehiculeCreateRequest;
import ma.covoiturage.covoiturage.dto.response.VehiculeResponse;
import ma.covoiturage.covoiturage.entity.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "trajets" , ignore = true)
    @Mapping(target = "conducteur" , ignore = true)
    Vehicule toEntity(VehiculeCreateRequest request);

    @Mapping(source = "condicteur.id" , target = "condicteurId")
    VehiculeResponse toResponse(Vehicule vehicule);
}

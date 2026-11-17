package ma.covoiturage.covoiturage.mapper;

import ma.covoiturage.covoiturage.dto.request.VehiculeCreateRequest;
import ma.covoiturage.covoiturage.dto.request.VehiculeUpdateRequest;
import ma.covoiturage.covoiturage.dto.response.VehiculeResponse;
import ma.covoiturage.covoiturage.entity.Vehicule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "trajets" , ignore = true)
    @Mapping(target = "conducteur" , ignore = true)
    Vehicule toEntity(VehiculeCreateRequest request);

    @Mapping(source = "conducteur.id" , target = "conducteurId")
    VehiculeResponse toResponse(Vehicule vehicule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id" , ignore = true)
    void updateVehicule(VehiculeUpdateRequest request , @MappingTarget Vehicule vehicule);
}

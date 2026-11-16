package ma.covoiturage.covoiturage.mapper;

import ma.covoiturage.covoiturage.dto.request.ConducteurCreateRequest;
import ma.covoiturage.covoiturage.dto.request.UserCreateRequest;
import ma.covoiturage.covoiturage.dto.response.UserResponse;
import ma.covoiturage.covoiturage.entity.Admin;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Passager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Passager toPassager(UserCreateRequest request);

    @Mapping(target = "id" , ignore = true)
    Admin toAdmin(UserCreateRequest request);

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    @Mapping(target = "trajets", ignore = true)
    Conducteur toConducteur(ConducteurCreateRequest request);

    @Mapping(target = "role" , constant = "PASSAGER")
    @Mapping(target = "numeroPermis" , ignore = true)
    UserResponse toPassagerResponse(Passager passager);

    @Mapping(target = "role" , constant = "ADMIN")
    @Mapping(target = "numeroPermis" , ignore = true)
    UserResponse toAdminResponse(Admin admin);

    @Mapping(target = "role" ,constant = "CONDUCTEUR")
    UserResponse toConducteurResponse(Conducteur conducteur);

}

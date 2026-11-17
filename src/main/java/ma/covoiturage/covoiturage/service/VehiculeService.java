package ma.covoiturage.covoiturage.service;

import ma.covoiturage.covoiturage.dto.request.VehiculeCreateRequest;
import ma.covoiturage.covoiturage.dto.request.VehiculeUpdateRequest;
import ma.covoiturage.covoiturage.dto.response.VehiculeResponse;
import java.util.List;
import java.util.UUID;

public interface VehiculeService {
    VehiculeResponse createVehicule(VehiculeCreateRequest request);

    VehiculeResponse updateVehicule(long id, VehiculeUpdateRequest request);

    void deleteVehicule(long id);

    VehiculeResponse getVehiculeById(long id);

    List<VehiculeResponse> getVehiculesByConducteurId(UUID id);

    List<VehiculeResponse> getAllVehicules();
}

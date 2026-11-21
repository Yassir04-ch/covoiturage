package ma.covoiturage.covoiturage.service;

import ma.covoiturage.covoiturage.dto.request.RechercheTrajetRequest;
import ma.covoiturage.covoiturage.dto.request.TrajetCreateRequest;
import ma.covoiturage.covoiturage.dto.response.TrajetResponse;
import ma.covoiturage.covoiturage.entity.Trajet;

import java.util.List;
import java.util.UUID;

public interface TrajetService {
    TrajetResponse createTrajet(TrajetCreateRequest request);

    void deleteTrajet(long id, UUID conducteurId);

    TrajetResponse getTrajetById(long id);

    List<TrajetResponse> getTrajetsByConducteurId(UUID conducteurId);

    List<TrajetResponse> getAllTrajets();

    void updateStatutTrajet(Trajet trajet);

    TrajetResponse terminerTrajet(long id , UUID conducteurId);

    TrajetResponse annulerTrajet(long id , UUID conducteurId);

    List<TrajetResponse> rechercherTrajets(RechercheTrajetRequest request);
}

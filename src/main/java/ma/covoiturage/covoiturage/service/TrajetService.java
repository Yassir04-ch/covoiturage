package ma.covoiturage.covoiturage.service;

import ma.covoiturage.covoiturage.dto.request.RechercheTrajetRequest;
import ma.covoiturage.covoiturage.dto.request.TrajetCreateRequest;
import ma.covoiturage.covoiturage.dto.response.TrajetResponse;
import ma.covoiturage.covoiturage.entity.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TrajetService {
    TrajetResponse createTrajet(TrajetCreateRequest request);

    void deleteTrajet(long id, UUID conducteurId);

    TrajetResponse getTrajetById(long id);

    Page<TrajetResponse> getTrajetsByConducteurId(UUID conducteurId , Pageable pageable);

    Page<TrajetResponse> getAllTrajets(Pageable pageable);

    void updateStatutTrajet(Trajet trajet);

    TrajetResponse terminerTrajet(long id , UUID conducteurId);

    TrajetResponse annulerTrajet(long id , UUID conducteurId);

    Page<TrajetResponse> rechercherTrajets(RechercheTrajetRequest request , Pageable pageable);
}

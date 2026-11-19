package ma.covoiturage.covoiturage.service;

import ma.covoiturage.covoiturage.dto.request.PointArretCreateRequest;
import ma.covoiturage.covoiturage.dto.response.PointArretResponse;
import ma.covoiturage.covoiturage.entity.Trajet;

import java.util.List;

public interface PointArretService {
    void createPointArret(List<PointArretCreateRequest> requests , Trajet trajet);
}

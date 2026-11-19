package ma.covoiturage.covoiturage.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.PointArretCreateRequest;
import ma.covoiturage.covoiturage.dto.response.PointArretResponse;
import ma.covoiturage.covoiturage.entity.PointArret;
import ma.covoiturage.covoiturage.entity.Trajet;
import ma.covoiturage.covoiturage.exception.InvalideException;
import ma.covoiturage.covoiturage.mapper.PointArretMapper;
import ma.covoiturage.covoiturage.repository.PointArretRepository;
import ma.covoiturage.covoiturage.service.PointArretService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PointArretServiceImp implements PointArretService {
    private final PointArretMapper pointArretMapper;

    @Override
    @Transactional
    public void createPointArret(List<PointArretCreateRequest> requests, Trajet trajet) {
        validePoitArret(requests);
        List<PointArret> pointArrets = requests.stream().map(pointArretMapper::toEntity).toList();
        pointArrets.forEach(point -> point.setTrajet(trajet));
        trajet.getPointArrets().addAll(pointArrets);
    }

    public void validePoitArret(List<PointArretCreateRequest> pointArrets){
        List<PointArretCreateRequest> pointsSorted = pointArrets.stream().sorted(Comparator.comparing(PointArretCreateRequest::getOrdre)).toList();
        Set<String> villes = new HashSet<>();
        for(int i = 0 ; i < pointsSorted.size() ; i++){
            if(!villes.add(pointsSorted.get(i).getVille().trim().toLowerCase(Locale.ROOT))){
                throw  new InvalideException("Une ville ne peut pas apparaitre plusieurs fois");
            }
            if(i > 0){
                if(pointsSorted.get(i).getHeurePassage().isBefore(pointsSorted.get(i - 1).getHeurePassage())
                   || pointsSorted.get(i).getHeurePassage().equals(pointsSorted.get(i - 1).getHeurePassage())){
                    throw  new InvalideException("Heure de passage doivent étre croissantes");
                }
                if (pointsSorted.get(i).getPrixDepuisDepart().compareTo(pointsSorted.get(i - 1).getPrixDepuisDepart()) <= 0) {
                    throw new InvalideException("le prix doivent étre croissants");
                }
            }
        }
    }
}

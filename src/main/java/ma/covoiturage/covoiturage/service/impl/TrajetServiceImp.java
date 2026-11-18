package ma.covoiturage.covoiturage.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.TrajetCreateRequest;
import ma.covoiturage.covoiturage.dto.response.TrajetResponse;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Trajet;
import ma.covoiturage.covoiturage.entity.Vehicule;
import ma.covoiturage.covoiturage.entity.enums.StatutTrajet;
import ma.covoiturage.covoiturage.exception.ForbiddenException;
import ma.covoiturage.covoiturage.exception.InvalideException;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.mapper.TrajetMapper;
import ma.covoiturage.covoiturage.repository.ConducteurRepository;
import ma.covoiturage.covoiturage.repository.TrajetRepository;
import ma.covoiturage.covoiturage.repository.VehiculeRepository;
import ma.covoiturage.covoiturage.service.TrajetService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrajetServiceImp implements TrajetService {
    private final TrajetRepository trajetRepository;
    private final ConducteurRepository conducteurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetMapper trajetMapper;

    @Override
    @Transactional
    public TrajetResponse createTrajet(TrajetCreateRequest request) {
        Conducteur conducteur = conducteurRepository.findById(request.getConducteurId())
                .orElseThrow(()->new ResourceNotFoundException("conducteur not found"));
        Vehicule vehicule = vehiculeRepository.findById(request.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule not found"));
        if(!conducteur.getId().equals(vehicule.getConducteur().getId())){
            throw new ForbiddenException("véhicule n'appartient pas à ce conducteur");
        }
        valideDateAndPlaces(request.getDateDepart() , request.getPlacesProposees() , vehicule.getCapacite());
        Trajet trajet = trajetMapper.toEntity(request);
        trajet.setStatut(StatutTrajet.PUBLIE);
        trajet.setConducteur(conducteur);
        trajet.setVehicule(vehicule);
        return trajetMapper.toResponse(trajetRepository.save(trajet));
    }

    public void valideDateAndPlaces(LocalDateTime dateDepart , int placesProposees , int vehiculeCapacite){
        if(dateDepart == null || !dateDepart.isAfter(LocalDateTime.now())){
            throw new InvalideException("date de départ et invalide");
        }
        if (placesProposees > vehiculeCapacite) {
            throw new InvalideException("nombre de places dépasse la capacité du véhicule");
        }
    }

    @Override
    @Transactional
    public void deleteTrajet(long id, UUID conducteurId) {
        Trajet trajet = trajetRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Trajét not found"));
        if(!trajet.getConducteur().getId().equals(conducteurId)){
            throw new ForbiddenException("Vous n'étes pas autorisé à supprimer ce trajet");
        }
        trajetRepository.delete(trajet);
    }

    @Override
    @Transactional
    public TrajetResponse getTrajetById(long id) {
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajét not found"));
        return trajetMapper.toResponse(trajet);
    }

    @Override
    @Transactional
    public List<TrajetResponse> getTrajetsByConducteurId(UUID conducteurId) {
        List<Trajet> trajets = trajetRepository.findByConducteurId(conducteurId);
        return trajets.stream().map(trajetMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public List<TrajetResponse> getAllTrajets() {
        List<Trajet> trajets = trajetRepository.findAll();
        return trajets.stream().map(trajetMapper::toResponse).toList();
    }


}

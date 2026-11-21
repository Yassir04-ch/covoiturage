package ma.covoiturage.covoiturage.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.RechercheTrajetRequest;
import ma.covoiturage.covoiturage.dto.request.TrajetCreateRequest;
import ma.covoiturage.covoiturage.dto.response.TrajetResponse;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Reservation;
import ma.covoiturage.covoiturage.entity.Trajet;
import ma.covoiturage.covoiturage.entity.Vehicule;
import ma.covoiturage.covoiturage.entity.enums.StatutReservation;
import ma.covoiturage.covoiturage.entity.enums.StatutTrajet;
import ma.covoiturage.covoiturage.exception.ForbiddenException;
import ma.covoiturage.covoiturage.exception.InvalideException;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.mapper.TrajetMapper;
import ma.covoiturage.covoiturage.repository.ConducteurRepository;
import ma.covoiturage.covoiturage.repository.ReservationRepository;
import ma.covoiturage.covoiturage.repository.TrajetRepository;
import ma.covoiturage.covoiturage.repository.VehiculeRepository;
import ma.covoiturage.covoiturage.service.PointArretService;
import ma.covoiturage.covoiturage.service.TrajetService;
import ma.covoiturage.covoiturage.specification.TrajetSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrajetServiceImp implements TrajetService {
    private final TrajetRepository trajetRepository;
    private final ConducteurRepository conducteurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final PointArretService pointArretService;
    private final TrajetMapper trajetMapper;
    private final ReservationRepository reservationRepository;

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

        pointArretService.createPointArret(request.getPointArrets(),trajet);
        TrajetResponse response = trajetMapper.toResponse(trajetRepository.save(trajet));
        return efectPlacesDisponiblesToResponse(response , trajet);
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
        TrajetResponse response = trajetMapper.toResponse(trajet);
        return efectPlacesDisponiblesToResponse(response , trajet);
    }

    @Override
    @Transactional
    public List<TrajetResponse> getTrajetsByConducteurId(UUID conducteurId) {
        List<Trajet> trajets = trajetRepository.findByConducteurId(conducteurId);
        return trajets.stream().map(trajet -> efectPlacesDisponiblesToResponse(trajetMapper.toResponse(trajet),trajet)).toList();
    }

    @Override
    @Transactional
    public List<TrajetResponse> getAllTrajets() {
        List<Trajet> trajets = trajetRepository.findAll();
        return trajets.stream().map(trajet -> efectPlacesDisponiblesToResponse(trajetMapper.toResponse(trajet),trajet)).toList();
    }

    @Override
    @Transactional
    public void updateStatutTrajet(Trajet trajet){
        long totalPlacesReservees = reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(trajet.getId() , StatutReservation.CONFIRMEE);
        if(totalPlacesReservees >= trajet.getPlacesProposees()){
            trajet.setStatut(StatutTrajet.COMPLET);
        }else {
            trajet.setStatut(StatutTrajet.PUBLIE);
        }
    }

    @Override
    @Transactional
    public TrajetResponse terminerTrajet(long id , UUID conducteurId){
        Trajet trajet = valideTrajetAndConducteur(id , conducteurId);
        if(trajet.getDateDepart().isAfter(LocalDateTime.now())){
            throw new InvalideException("impossible de terminer le trajet avant l'heure de départ");
        }
        trajet.setStatut(StatutTrajet.TERMINE);
        TrajetResponse response = trajetMapper.toResponse(trajet);
        return efectPlacesDisponiblesToResponse(response , trajet);
    }


    @Override
    @Transactional
    public TrajetResponse annulerTrajet(long id, UUID conducteurId) {
        Trajet trajet = valideTrajetAndConducteur(id , conducteurId);
        trajet.setStatut(StatutTrajet.ANNULE);
        List<Reservation> reservations = reservationRepository.findByTrajetIdAndStatut(trajet.getId() , StatutReservation.CONFIRMEE);
        reservations.forEach(e -> e.setStatut(StatutReservation.ANNULEE));
        TrajetResponse response = trajetMapper.toResponse(trajet);
        return efectPlacesDisponiblesToResponse(response , trajet);
    }

    public Trajet valideTrajetAndConducteur(long id , UUID conducteurId){
        Trajet trajet = trajetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet not found"));
        if(!trajet.getConducteur().getId().equals(conducteurId)){
            throw new ForbiddenException("Vous n'étes pas autorisé à annulée ou terminée  ce trajet ");
        }
        if(trajet.getStatut() != StatutTrajet.PUBLIE && trajet.getStatut() != StatutTrajet.COMPLET){
            throw  new InvalideException("Seuls les trajets publiés ou complets peuvent étre annulés ou terminés");
        }
        return trajet;
    }

    public TrajetResponse efectPlacesDisponiblesToResponse(TrajetResponse response , Trajet trajet){
        long placeReserve = reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(trajet.getId() , StatutReservation.CONFIRMEE);
        long placesDisponibles = trajet.getPlacesProposees() - placeReserve;
        response.setPlacesDisponibles(placesDisponibles);
        return response;
    }

    @Override
    @Transactional
    public List<TrajetResponse> rechercherTrajets(RechercheTrajetRequest request) {
        if(request.getVilleDepart().equalsIgnoreCase(request.getVilleArrivee())){
            throw new InvalideException("La ville de départ doit être différente de la ville d'arrivée");
        }
        var specification = TrajetSpecification.rechercherTrajets(request.getVilleDepart() , request.getVilleArrivee(),request.getDate());
        List<Trajet> trajets = trajetRepository.findAll(specification);
        return trajets.stream().map(trajet -> efectPlacesDisponiblesToResponse(trajetMapper.toResponse(trajet),trajet)).toList();
    }

}

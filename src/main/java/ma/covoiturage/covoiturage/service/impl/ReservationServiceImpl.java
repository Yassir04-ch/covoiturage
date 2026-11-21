package ma.covoiturage.covoiturage.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.ReservationCreateRequest;
import ma.covoiturage.covoiturage.dto.response.ReservationResponse;
import ma.covoiturage.covoiturage.entity.Passager;
import ma.covoiturage.covoiturage.entity.PointArret;
import ma.covoiturage.covoiturage.entity.Reservation;
import ma.covoiturage.covoiturage.entity.Trajet;
import ma.covoiturage.covoiturage.entity.enums.StatutReservation;
import ma.covoiturage.covoiturage.entity.enums.StatutTrajet;
import ma.covoiturage.covoiturage.exception.ForbiddenException;
import ma.covoiturage.covoiturage.exception.InvalideException;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.mapper.ReservationMapper;
import ma.covoiturage.covoiturage.repository.PassagerRepository;
import ma.covoiturage.covoiturage.repository.PointArretRepository;
import ma.covoiturage.covoiturage.repository.ReservationRepository;
import ma.covoiturage.covoiturage.repository.TrajetRepository;
import ma.covoiturage.covoiturage.service.ReservationService;
import ma.covoiturage.covoiturage.service.TrajetService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final PassagerRepository passagerRepository;
    private final TrajetRepository trajetRepository;
    private final PointArretRepository pointArretRepository;
    private final ReservationMapper reservationMapper;
    private final TrajetService trajetService;

    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request) {
        Passager passager = passagerRepository.findById(request.getPassagerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Passager not found"));
        Trajet trajet = trajetRepository.findById(request.getTrajetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trajet not found"));
        PointArret pointMontee = pointArretRepository.findById(request.getPointMonteeId())
                .orElseThrow(() -> new ResourceNotFoundException("Point Montee not found"));
        PointArret pointDescente = pointArretRepository.findById(request.getPointDescenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Point Descente not found"));
        ValideTrajet(trajet , request.getNombrePlaces());
        validePassager(passager.getId() ,trajet.getId());
        validePoints(pointMontee , pointDescente , trajet);
        BigDecimal prixTotal = calculerMontantTotal(pointMontee , pointDescente , request.getNombrePlaces());

        Reservation reservation = reservationMapper.toEntity(request);
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setMontantTotal(prixTotal);
        reservation.setPointMontee(pointMontee);
        reservation.setPointDescente(pointDescente);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        Reservation saveReserv = reservationRepository.save(reservation);
        trajetService.updateStatutTrajet(trajet);
        return reservationMapper.toResponse(saveReserv);
    }

    public void ValideTrajet(Trajet trajet,int nombrePlaces){
        if(!trajet.getStatut().equals(StatutTrajet.PUBLIE)){
            throw new InvalideException("trajet n'est pas disponible pour la réservation");
        }
        long totalPlaceReserver = reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(trajet.getId(), StatutReservation.CONFIRMEE);
        if(totalPlaceReserver >= trajet.getPlacesProposees()
                ||  nombrePlaces + totalPlaceReserver > trajet.getPlacesProposees()){
            throw new InvalideException("Nombre de places insuffisant");
        }
    }

    public void validePassager(UUID passagerId , long trajetId){
        boolean valide = reservationRepository.existsByPassagerIdAndTrajetIdAndStatut(passagerId , trajetId , StatutReservation.CONFIRMEE);
        if(valide){
            throw new InvalideException("Vous avez déjà une réservation confirmée pour ce trajet");
        }
    }

    public BigDecimal calculerMontantTotal(PointArret pointMontee , PointArret pointDescente , int nombrePlaces){
        BigDecimal prix = pointDescente.getPrixDepuisDepart().subtract(pointMontee.getPrixDepuisDepart());
        return prix.multiply(BigDecimal.valueOf(nombrePlaces));
    }

    public void validePoints(PointArret pointMontee , PointArret pointDescente , Trajet trajet){
        if(pointMontee.getTrajet().getId() != trajet.getId()
            || pointDescente.getTrajet().getId() != trajet.getId()){
            throw new InvalideException("Les points d'arrét ne correspondent pas au trajet sélectionné");
        }
        if(pointMontee.getOrdre() >= pointDescente.getOrdre()){
            throw new InvalideException("Le point de montée doit précéder le point de descente");
        }
    }

    @Override
    @Transactional
    public ReservationResponse annulerReservation(long reservationId , UUID passagerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        if(reservation.getStatut() == StatutReservation.ANNULEE){
            throw  new InvalideException("Reservation est déja annulée");
        }
        if(!reservation.getPassager().getId().equals(passagerId)){
            throw new ForbiddenException("Vous n'étes pas autorisé à annuleée cette reservation");
        }
        Trajet trajet = reservation.getTrajet();
        long delai = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getTrajet().getDateDepart());
        if(delai < 24){
            throw new InvalideException("impossible annuler reservation : délai minimum de 24 heures avant le départ ");
        }
        reservation.setStatut(StatutReservation.ANNULEE);
        trajetService.updateStatutTrajet(trajet);
        return reservationMapper.toResponse(reservation);
    }

    @Override
    @Transactional
    public List<ReservationResponse> getAllReservation(){
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(reservationMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public List<ReservationResponse> getReservationsByPassager(UUID passagerId){
        List<Reservation> reservations = reservationRepository.getReservationsByPassagerId(passagerId);
        return reservations.stream().map(reservationMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public List<ReservationResponse> getReservationsByTrajet(long trajetId){
        List<Reservation> reservations = reservationRepository.getReservationsByTrajetId(trajetId);
        return reservations.stream().map(reservationMapper::toResponse).toList();
    }

}

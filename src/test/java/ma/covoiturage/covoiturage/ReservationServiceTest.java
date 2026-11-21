
package ma.covoiturage.covoiturage;

import ma.covoiturage.covoiturage.dto.request.ReservationCreateRequest;
import ma.covoiturage.covoiturage.dto.response.ReservationResponse;
import ma.covoiturage.covoiturage.entity.*;
import ma.covoiturage.covoiturage.entity.enums.StatutReservation;
import ma.covoiturage.covoiturage.entity.enums.StatutTrajet;
import ma.covoiturage.covoiturage.exception.ForbiddenException;
import ma.covoiturage.covoiturage.exception.InvalideException;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.mapper.ReservationMapper;
import ma.covoiturage.covoiturage.repository.*;
import ma.covoiturage.covoiturage.service.TrajetService;
import ma.covoiturage.covoiturage.service.impl.ReservationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PassagerRepository passagerRepository;

    @Mock
    private TrajetRepository trajetRepository;

    @Mock
    private PointArretRepository pointArretRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private TrajetService trajetService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Passager passager;
    private Trajet trajet;
    private PointArret pointMontee;
    private PointArret pointDescente;
    private ReservationCreateRequest request;
    private Reservation reservation;
    private UUID passagerId;
    private ReservationResponse response;

    @BeforeEach
    void setUp() {
        passagerId = UUID.randomUUID();
        passager = new Passager();
        passager.setId(passagerId);
        trajet = new Trajet();
        trajet.setId(1L);
        trajet.setStatut(StatutTrajet.PUBLIE);
        trajet.setPlacesProposees(4);
        trajet.setDateDepart(LocalDateTime.now().plusDays(3));
        pointMontee = new PointArret();
        pointMontee.setId(1L);
        pointMontee.setOrdre(1);
        pointMontee.setPrixDepuisDepart(new BigDecimal("20"));
        pointMontee.setTrajet(trajet);
        pointDescente = new PointArret();
        pointDescente.setId(2L);
        pointDescente.setOrdre(2);
        pointDescente.setPrixDepuisDepart(new BigDecimal("50"));
        pointDescente.setTrajet(trajet);
        request = new ReservationCreateRequest();
        request.setPassagerId(passagerId);
        request.setTrajetId(1L);
        request.setPointMonteeId(1L);
        request.setPointDescenteId(2L);
        request.setNombrePlaces(2);
        reservation = new Reservation();
    }

    @Test
    void createReservationSuccess() {
        response = new ReservationResponse();
        when(passagerRepository.findById(passagerId)).thenReturn(Optional.of(passager));
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(pointArretRepository.findById(1L)).thenReturn(Optional.of(pointMontee));
        when(pointArretRepository.findById(2L)).thenReturn(Optional.of(pointDescente));
        when(reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(1L, StatutReservation.CONFIRMEE)).thenReturn(0L);
        when(reservationRepository.existsByPassagerIdAndTrajetIdAndStatut(passagerId, 1L, StatutReservation.CONFIRMEE)).thenReturn(false);
        when(reservationMapper.toEntity(request)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.toResponse(reservation)).thenReturn(response);

        ReservationResponse result = reservationService.createReservation(request);

        assertNotNull(result);
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
        assertEquals(new BigDecimal("60"), reservation.getMontantTotal());
        assertEquals(passager, reservation.getPassager());
        assertEquals(trajet, reservation.getTrajet());
        verify(reservationRepository).save(reservation);
        verify(trajetService).updateStatutTrajet(trajet);
    }

    @Test
    void createReservationPassagerNotFound() {
        when(passagerRepository.findById(passagerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservationTrajetNotFound() {
        when(passagerRepository.findById(passagerId)).thenReturn(Optional.of(passager));
        when(trajetRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_trajetNotPublished() {
        trajet.setStatut(StatutTrajet.COMPLET);
        when(passagerRepository.findById(passagerId)).thenReturn(Optional.of(passager));
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(pointArretRepository.findById(1L)).thenReturn(Optional.of(pointMontee));
        when(pointArretRepository.findById(2L)).thenReturn(Optional.of(pointDescente));
        assertThrows(InvalideException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservationPassagerAlreadyHasReservation() {
        when(passagerRepository.findById(passagerId)).thenReturn(Optional.of(passager));
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(pointArretRepository.findById(1L)).thenReturn(Optional.of(pointMontee));
        when(pointArretRepository.findById(2L)).thenReturn(Optional.of(pointDescente));
        when(reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(1L, StatutReservation.CONFIRMEE)).thenReturn(0L);
        when(reservationRepository.existsByPassagerIdAndTrajetIdAndStatut(passagerId, 1L, StatutReservation.CONFIRMEE)).thenReturn(true);
        assertThrows(InvalideException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservationInvalidPoints() {
        pointMontee.setOrdre(2);
        pointDescente.setOrdre(1);
        when(passagerRepository.findById(passagerId)).thenReturn(Optional.of(passager));
        when(trajetRepository.findById(1L)).thenReturn(Optional.of(trajet));
        when(pointArretRepository.findById(1L)).thenReturn(Optional.of(pointMontee));
        when(pointArretRepository.findById(2L)).thenReturn(Optional.of(pointDescente));
        when(reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(1L, StatutReservation.CONFIRMEE)).thenReturn(0L);
        when(reservationRepository.existsByPassagerIdAndTrajetIdAndStatut(passagerId, 1L, StatutReservation.CONFIRMEE)).thenReturn(false);
        assertThrows(InvalideException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void annulerReservation() {
        response = new ReservationResponse();
        reservation.setId(1L);
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toResponse(reservation)).thenReturn(response);

        ReservationResponse result = reservationService.annulerReservation(1L, passagerId);

        assertNotNull(result);
        assertEquals(StatutReservation.ANNULEE, reservation.getStatut());
        verify(trajetService).updateStatutTrajet(trajet);
    }

    @Test
    void annulerReservationnotPassager() {
        UUID autrePassagerId = UUID.randomUUID();
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setStatut(StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(ForbiddenException.class, () -> reservationService.annulerReservation(1L, autrePassagerId));
        verify(trajetService, never()).updateStatutTrajet(any());
    }

    @Test
    void annulerReservationNonValideHours() {
        trajet.setDateDepart(LocalDateTime.now().plusHours(10));
        reservation.setPassager(passager);
        reservation.setTrajet(trajet);
        reservation.setStatut(StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(InvalideException.class, () -> reservationService.annulerReservation(1L, passagerId));
        verify(trajetService, never()).updateStatutTrajet(any());
    }

    @Test
    void getAllReservation() {
        response = new ReservationResponse();
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        when(reservationMapper.toResponse(reservation)).thenReturn(response);
        List<ReservationResponse> result = reservationService.getAllReservation();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
        verify(reservationRepository).findAll();
        verify(reservationMapper).toResponse(reservation);
    }

    @Test
    void getReservationsByPassager() {
        response = new ReservationResponse();
        when(reservationRepository.getReservationsByPassagerId(passagerId)).thenReturn(List.of(reservation));
        when(reservationMapper.toResponse(reservation)).thenReturn(response);
        List<ReservationResponse> result = reservationService.getReservationsByPassager(passagerId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
        verify(reservationRepository).getReservationsByPassagerId(passagerId);
        verify(reservationMapper).toResponse(reservation);
    }

    @Test
    void getReservationsByTrajet() {
        when(reservationRepository.getReservationsByTrajetId(1L)).thenReturn(List.of(reservation));
        when(reservationMapper.toResponse(reservation)).thenReturn(response);
        List<ReservationResponse> result = reservationService.getReservationsByTrajet(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
        verify(reservationRepository).getReservationsByTrajetId(1L);
        verify(reservationMapper).toResponse(reservation);
    }


}

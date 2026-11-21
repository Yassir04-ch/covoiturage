package ma.covoiturage.covoiturage;

import ma.covoiturage.covoiturage.dto.request.TrajetCreateRequest;
import ma.covoiturage.covoiturage.dto.response.TrajetResponse;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Trajet;
import ma.covoiturage.covoiturage.entity.Vehicule;
import ma.covoiturage.covoiturage.entity.enums.StatutReservation;
import ma.covoiturage.covoiturage.entity.enums.StatutTrajet;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.mapper.TrajetMapper;
import ma.covoiturage.covoiturage.repository.ConducteurRepository;
import ma.covoiturage.covoiturage.repository.ReservationRepository;
import ma.covoiturage.covoiturage.repository.TrajetRepository;
import ma.covoiturage.covoiturage.repository.VehiculeRepository;
import ma.covoiturage.covoiturage.service.impl.PointArretServiceImp;
import ma.covoiturage.covoiturage.service.impl.TrajetServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrajetServiceTest {
    @Mock
    private TrajetRepository trajetRepository;
    @Mock
    private ConducteurRepository conducteurRepository;
    @Mock
    private VehiculeRepository vehiculeRepository;
    @Mock
    private TrajetMapper trajetMapper;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private PointArretServiceImp pointArretService;
    @InjectMocks
    private TrajetServiceImp trajetService;

    private TrajetCreateRequest request;
    private TrajetResponse response;
    private Trajet trajet;
    private Conducteur conducteur;
    private Vehicule vehicule;

    UUID conducteurId;
    Long vehiculeId;
    long trajetId;

    @BeforeEach
    void setUp() {
        conducteurId = UUID.randomUUID();
        vehiculeId = 1L;
        trajetId = 1L;
        trajet = new Trajet();
        trajet.setId(trajetId);
        trajet.setConducteur(conducteur);
        trajet.setPlacesProposees(3);
        conducteur = new Conducteur();
        conducteur.setId(conducteurId);
        trajet.setConducteur(conducteur);
        vehicule = new Vehicule();
        vehicule.setId(vehiculeId);
        vehicule.setCapacite(4);
        vehicule.setConducteur(conducteur);
        request = TrajetCreateRequest.builder()
                .dateDepart(LocalDateTime.now().plusDays(2))
                .placesProposees(3)
                .conducteurId(conducteurId)
                .vehiculeId(vehiculeId)
                .build();
        response = TrajetResponse.builder()
                .id(trajetId)
                .dateDepart(request.getDateDepart())
                .placesProposees(3)
                .statut(StatutTrajet.PUBLIE.name())
                .conducteurId(conducteurId)
                .vehiculeId(vehiculeId)
                .build();
    }

    @Test
    void createTrajet(){
        when(conducteurRepository.findById(conducteurId)).thenReturn(Optional.of(conducteur));
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        when(trajetMapper.toEntity(request)).thenReturn(trajet);
        when(trajetMapper.toResponse(trajet)).thenReturn(response);
        when(trajetRepository.save(trajet)).thenReturn(trajet);

        TrajetResponse result = trajetService.createTrajet(request);

        assertNotNull(result);
        assertEquals(trajetId,result.getId());
        assertEquals(StatutTrajet.PUBLIE.name(),result.getStatut());
        assertEquals(conducteurId,result.getConducteurId());
        assertEquals(vehiculeId,result.getVehiculeId());
        assertSame(conducteur,trajet.getConducteur());
        assertSame(vehicule, trajet.getVehicule());

        verify(conducteurRepository).findById(conducteurId);
        verify(vehiculeRepository).findById(vehiculeId);
        verify(trajetMapper).toEntity(request);
        verify(trajetRepository).save(trajet);
        verify(trajetMapper).toResponse(trajet);
    }

    @Test
    void createTrajetConducteurNotFound() {
        when(conducteurRepository.findById(conducteurId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> trajetService.createTrajet(request));
        verify(vehiculeRepository, never()).findById(anyLong());
        verify(trajetRepository, never()).save(any());
        verify(trajetMapper, never()).toEntity(any());
    }

    @Test
    void createTrajetVehiculeNotFound() {
        when(conducteurRepository.findById(conducteurId)).thenReturn(Optional.of(conducteur));
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> trajetService.createTrajet(request));
        verify(trajetRepository, never()).save(any());
        verify(trajetMapper, never()).toEntity(any());
    }

    @Test
    void deleteTrajet(){
        when(trajetRepository.findById(trajetId)).thenReturn(Optional.of(trajet));
        trajetService.deleteTrajet(trajetId , conducteurId);

        verify(trajetRepository).findById(trajetId);
        verify(trajetRepository).delete(trajet);
    }

    @Test
    void trajedeletNotFound(){
        when(trajetRepository.findById(trajetId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> trajetService.deleteTrajet(trajetId ,conducteurId));
        verify(trajetRepository, never()).delete((Trajet) any());
    }

    @Test
    void getTrajetById() {
        when(trajetRepository.findById(trajetId)).thenReturn(Optional.of(trajet));
        when(trajetMapper.toResponse(trajet)).thenReturn(response);
        when(reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(trajetId, StatutReservation.CONFIRMEE)).thenReturn(1L);
        TrajetResponse result = trajetService.getTrajetById(trajetId);
        assertNotNull(result);
        assertEquals(response, result);
        assertEquals(2L, result.getPlacesDisponibles());
        verify(trajetRepository).findById(trajetId);
        verify(trajetMapper).toResponse(trajet);
    }

    @Test
    void getTrajetByIdNotFound() {
        when(trajetRepository.findById(trajetId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> trajetService.getTrajetById(trajetId));
        verify(trajetMapper, never()).toResponse(any());
    }

    @Test
    void updateStatutTrajetComplet() {
        when(reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(trajetId, StatutReservation.CONFIRMEE)).thenReturn(3L);
        trajet.setId(trajetId);
        trajet.setPlacesProposees(3);
        trajetService.updateStatutTrajet(trajet);
        assertEquals(StatutTrajet.COMPLET, trajet.getStatut());
    }

    @Test
    void updateStatutTrajetPublie() {
        when(reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(trajetId, StatutReservation.CONFIRMEE)).thenReturn(2L);
        trajet.setId(trajetId);
        trajet.setPlacesProposees(3);
        trajetService.updateStatutTrajet(trajet);
        assertEquals(StatutTrajet.PUBLIE, trajet.getStatut());
    }

    @Test
    void terminerTrajet() {
        trajet.setId(trajetId);
        trajet.setStatut(StatutTrajet.PUBLIE);
        trajet.setDateDepart(LocalDateTime.now().minusHours(1));
        when(trajetRepository.findById(trajetId)).thenReturn(Optional.of(trajet));
        when(trajetMapper.toResponse(trajet)).thenReturn(response);
        when(reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(trajetId, StatutReservation.CONFIRMEE)).thenReturn(1L);
        TrajetResponse result = trajetService.terminerTrajet(trajetId, conducteurId);
        assertNotNull(result);
        assertEquals(StatutTrajet.TERMINE, trajet.getStatut());
        assertEquals(2, result.getPlacesDisponibles());
        verify(trajetRepository).findById(trajetId);
        verify(trajetMapper).toResponse(trajet);
    }

    @Test
    void annulerTrajet() {
        trajet.setId(trajetId);
        trajet.setStatut(StatutTrajet.PUBLIE);
        when(trajetRepository.findById(trajetId)).thenReturn(Optional.of(trajet));
        when(reservationRepository.findByTrajetIdAndStatut(trajetId, StatutReservation.CONFIRMEE)).thenReturn(List.of());
        when(trajetMapper.toResponse(trajet)).thenReturn(response);
        when(reservationRepository.countTotalNombrePlacesByTrajetIdAndStatut(trajetId, StatutReservation.CONFIRMEE)).thenReturn(0L);

        TrajetResponse result = trajetService.annulerTrajet(trajetId, conducteurId);
        assertNotNull(result);
        assertEquals(StatutTrajet.ANNULE, trajet.getStatut());
        verify(trajetRepository).findById(trajetId);
        verify(reservationRepository).findByTrajetIdAndStatut(trajetId, StatutReservation.CONFIRMEE);
    }
}

package ma.covoiturage.covoiturage;

import ma.covoiturage.covoiturage.dto.request.VehiculeCreateRequest;
import ma.covoiturage.covoiturage.dto.request.VehiculeUpdateRequest;
import ma.covoiturage.covoiturage.dto.response.VehiculeResponse;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Vehicule;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.mapper.VehiculeMapper;
import ma.covoiturage.covoiturage.repository.ConducteurRepository;
import ma.covoiturage.covoiturage.repository.VehiculeRepository;
import ma.covoiturage.covoiturage.service.impl.VehiculeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehiculeServiceTest {
    @Mock
    VehiculeRepository vehiculeRepository;

    @Mock
    ConducteurRepository conducteurRepository;

    @Mock
    VehiculeMapper vehiculeMapper;

    @InjectMocks
    VehiculeServiceImpl vehiculeService;

    VehiculeCreateRequest request;
    VehiculeUpdateRequest updateRequest;
    VehiculeResponse response;
    Vehicule vehicule;
    Conducteur conducteur;
    Vehicule vehicule2;
    VehiculeResponse response2;


    UUID conducteurId;
    long vehiculeId;
    @BeforeEach
    void setUpd(){
        conducteurId = UUID.randomUUID();
        vehiculeId = 1L;
        request = VehiculeCreateRequest.builder()
                .marque("Dacia")
                        .capacite(4)
                .couleur("BLACK")
                .modele("2021")
                .conducteurId(conducteurId)
                .build();
        vehicule = new Vehicule();
        conducteur = new Conducteur();
        response = VehiculeResponse.builder()
                .id(vehiculeId)
                .marque("Dacia")
                .modele("2021")
                .couleur("BLACK")
                .capacite(4)
                .conducteurId(conducteurId)
                .build();
        updateRequest = VehiculeUpdateRequest.builder()
                .marque("Renault")
                .couleur("RED")
                .build();
        conducteur.setId(conducteurId);
        vehicule.setConducteur(conducteur);

        vehicule2 = new Vehicule();
        response2 = VehiculeResponse.builder()
                .marque("Renault")
                .couleur("RED")
                .modele("2024")
                .capacite(4)
                .immatriculation("A526788")
                .conducteurId(conducteurId)
                .build();
    }

    @Test
    void createVehiculeTest(){
        when(conducteurRepository.findById(conducteurId)).thenReturn(Optional.of(conducteur));
        when(vehiculeMapper.toEntity(request)).thenReturn(vehicule);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponse saved = vehiculeService.createVehicule(request);

        assertNotNull(saved);
        assertEquals("Dacia", saved.getMarque());
        assertEquals("2021", saved.getModele());
        assertEquals(4, saved.getCapacite());
        assertEquals(conducteurId, saved.getConducteurId());

        verify(conducteurRepository).findById(conducteurId);
        verify(vehiculeMapper).toEntity(request);
        verify(vehiculeRepository).save(vehicule);
        verify(vehiculeMapper).toResponse(vehicule);
    }

    @Test
    void createVehiculeConducteurNotFound(){
        when(conducteurRepository.findById(conducteurId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class , () -> vehiculeService.createVehicule(request));
        verify(vehiculeRepository,never()).save(any());
        verify(vehiculeMapper , never()).toEntity(any());
        verify(vehiculeMapper , never()).toResponse(any());
    }

    @Test
    void updateVehicule(){
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        VehiculeResponse update = vehiculeService.updateVehicule(vehiculeId , conducteurId , updateRequest);

        assertNotNull(update);
        assertSame(conducteurId , update.getConducteurId());
        verify(vehiculeRepository).findById(vehiculeId);
        verify(vehiculeMapper).updateVehicule(updateRequest , vehicule);
        verify(vehiculeMapper).toResponse(vehicule);
    }

    @Test
    void deleteVehicule(){
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));

        vehiculeService.deleteVehicule(vehiculeId);
        verify(vehiculeRepository).findById(vehiculeId);
        verify(vehiculeRepository).delete(vehicule);
    }

    @Test
    void getVehiculeById(){
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);

        vehiculeService.getVehiculeById(vehiculeId);

        verify(vehiculeRepository).findById(vehiculeId);
        verify(vehiculeMapper).toResponse(vehicule);
    }

    @Test
    void vehiculeNotfound(){
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class , () -> vehiculeService.getVehiculeById(vehiculeId));

        verify(vehiculeRepository).findById(vehiculeId);
        verify(vehiculeMapper,never()).toResponse(vehicule);

    }

    @Test
    void getVehiculesByConducteurId(){
        when(vehiculeRepository.findByConducteurId(conducteurId)).thenReturn(List.of(vehicule , vehicule2));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);
        when(vehiculeMapper.toResponse(vehicule2)).thenReturn(response2);

        List<VehiculeResponse> results = vehiculeService.getVehiculesByConducteurId(conducteurId);

        assertNotNull(results);
        assertEquals(2 , results.size());

        verify(vehiculeRepository).findByConducteurId(conducteurId);
        verify(vehiculeMapper).toResponse(vehicule);
        verify(vehiculeMapper).toResponse(vehicule2);
    }

    @Test
    void getAllVehicules(){
        when(vehiculeRepository.findAll()).thenReturn(List.of(vehicule , vehicule2));
        when(vehiculeMapper.toResponse(vehicule)).thenReturn(response);
        when(vehiculeMapper.toResponse(vehicule2)).thenReturn(response2);

        List<VehiculeResponse> result = vehiculeService.getAllVehicules();

        assertNotNull(result);
        assertEquals(2,result.size());

        verify(vehiculeRepository).findAll();
        verify(vehiculeMapper).toResponse(vehicule);
        verify(vehiculeMapper).toResponse(vehicule2);
    }


}

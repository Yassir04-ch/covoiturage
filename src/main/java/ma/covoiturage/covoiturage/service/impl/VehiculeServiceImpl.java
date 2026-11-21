package ma.covoiturage.covoiturage.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.VehiculeCreateRequest;
import ma.covoiturage.covoiturage.dto.request.VehiculeUpdateRequest;
import ma.covoiturage.covoiturage.dto.response.VehiculeResponse;
import ma.covoiturage.covoiturage.entity.Conducteur;
import ma.covoiturage.covoiturage.entity.Vehicule;
import ma.covoiturage.covoiturage.exception.ConducteurNotFound;
import ma.covoiturage.covoiturage.exception.ForbiddenException;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import ma.covoiturage.covoiturage.exception.VehiculeNotFound;
import ma.covoiturage.covoiturage.mapper.VehiculeMapper;
import ma.covoiturage.covoiturage.repository.ConducteurRepository;
import ma.covoiturage.covoiturage.repository.VehiculeRepository;
import ma.covoiturage.covoiturage.service.VehiculeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {
    private final VehiculeRepository vehiculeRepository;
    private final ConducteurRepository conducteurRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    @Transactional
    public VehiculeResponse createVehicule(VehiculeCreateRequest request){
        Conducteur conducteur = conducteurRepository.findById(request.getConducteurId())
                                .orElseThrow(() -> new ConducteurNotFound("Conducteur not found"));
        Vehicule vehicule = vehiculeMapper.toEntity(request);
        vehicule.setConducteur(conducteur);
        return vehiculeMapper.toResponse(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional
    public VehiculeResponse updateVehicule(long id,UUID conducteurId , VehiculeUpdateRequest request){
        Vehicule vehicule = vehiculeRepository.findById(id)
                            .orElseThrow(()->new VehiculeNotFound("Vehicule not Found"));
        if(!vehicule.getConducteur().getId().equals(conducteurId)){
            throw new ForbiddenException("Vous n'étes pas autorisé à modifier ce véhicule");
        }
        vehiculeMapper.updateVehicule(request , vehicule);
        return vehiculeMapper.toResponse(vehicule);
    }

    @Override
    @Transactional
    public void deleteVehicule(long id){
        Vehicule vehicule = vehiculeRepository.findById(id)
                            .orElseThrow(() -> new VehiculeNotFound("Vehicule note found"));
        vehiculeRepository.delete(vehicule);
    }

    @Override
    @Transactional
    public VehiculeResponse getVehiculeById(long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                            .orElseThrow(() -> new VehiculeNotFound("Vehicule note found"));
        return vehiculeMapper.toResponse(vehicule);
    }

    @Override
    @Transactional
    public List<VehiculeResponse> getVehiculesByConducteurId(UUID id) {
        List<Vehicule> vehicules = vehiculeRepository.findByConducteurId(id);
        return vehicules.stream().map(vehiculeMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public List<VehiculeResponse> getAllVehicules() {
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        return vehicules.stream().map(vehiculeMapper::toResponse).toList();
    }


}

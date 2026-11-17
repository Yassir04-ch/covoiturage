package ma.covoiturage.covoiturage.service.impl;

import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.repository.VehiculeRepository;
import ma.covoiturage.covoiturage.service.VehiculeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {
    private final VehiculeRepository vehiculeRepository;


}

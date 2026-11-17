package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehiculeRepository extends JpaRepository<Vehicule , Long>{
    List<Vehicule> findByConducteurId(UUID id);
}

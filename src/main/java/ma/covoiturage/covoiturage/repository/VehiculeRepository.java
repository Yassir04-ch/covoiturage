package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculeRepository extends JpaRepository<Vehicule , Long>{

}

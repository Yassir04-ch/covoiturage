package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConducteurRepository extends JpaRepository<Conducteur , UUID> {
}

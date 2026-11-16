package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Passager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PassagerRepository extends JpaRepository<Passager , UUID> {
}

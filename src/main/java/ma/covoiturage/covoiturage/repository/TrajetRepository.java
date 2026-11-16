package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrajetRepository extends JpaRepository<Trajet , Long>{
}

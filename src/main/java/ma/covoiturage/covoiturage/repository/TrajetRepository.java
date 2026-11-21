package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface TrajetRepository extends JpaRepository<Trajet , Long> , JpaSpecificationExecutor<Trajet> {
    Page<Trajet> findByConducteurId(UUID conducteruId , Pageable pageable);
}

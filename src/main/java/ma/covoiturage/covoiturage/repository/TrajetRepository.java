package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TrajetRepository extends JpaRepository<Trajet , Long> , JpaSpecificationExecutor<Trajet> {
    Page<Trajet> findByConducteurId(UUID conducteruId , Pageable pageable);

    @Query("""
        SELECT t FROM Trajet t
        JOIN t.pointArrets pm
        JOIN t.pointArrets pa
        WHERE pm.ordre < pa.ordre
        AND pa.prixDepuisDepart - pm.prixDepuisDepart <= :prixMax
        AND t.statut = "PUBLIE"
    """)
    Page<Trajet> filterTrajetByPrixMax(@Param("prixMax")BigDecimal prixMax , Pageable pageable);
}

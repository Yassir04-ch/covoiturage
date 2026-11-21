package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Reservation;
import ma.covoiturage.covoiturage.entity.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation , Long>{
    List<Reservation> findByTrajetIdAndStatut(long id , StatutReservation status);
    boolean existsByPassagerIdAndTrajetIdAndStatut(UUID passagerId, Long trajetId, StatutReservation statut);
    long countTotalNombrePlacesByTrajetIdAndStatut(long id , StatutReservation statut);
    List<Reservation> getReservationsByPassagerId(UUID passagerId);
    List<Reservation> getReservationsByTrajetId(long trajetId);
}

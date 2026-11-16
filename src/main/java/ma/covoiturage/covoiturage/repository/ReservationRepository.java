package ma.covoiturage.covoiturage.repository;

import ma.covoiturage.covoiturage.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation , Long>{
}

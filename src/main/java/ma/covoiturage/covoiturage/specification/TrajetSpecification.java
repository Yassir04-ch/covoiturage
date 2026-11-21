package ma.covoiturage.covoiturage.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import ma.covoiturage.covoiturage.entity.PointArret;
import ma.covoiturage.covoiturage.entity.Reservation;
import ma.covoiturage.covoiturage.entity.Trajet;
import ma.covoiturage.covoiturage.entity.enums.StatutReservation;
import ma.covoiturage.covoiturage.entity.enums.StatutTrajet;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Locale;

public class TrajetSpecification {

    public static Specification<Trajet> rechercherTrajets(String villeDepart , String villeArret , LocalDate date){
        return (root, query, criteriaBuilder) -> {
            Join<Trajet , PointArret> poitMonte = root.join("pointArrets");
            Join<Trajet , PointArret> pointDescente = root.join("pointArrets");

            var conditionDepart = criteriaBuilder.equal(criteriaBuilder.lower(poitMonte.get("ville")),villeDepart.trim().toLowerCase(Locale.ROOT));
            var conditionArret = criteriaBuilder.equal(criteriaBuilder.lower(pointDescente.get("ville")),villeArret.trim().toLowerCase(Locale.ROOT));
            var conditionOrdre = criteriaBuilder.lessThan(poitMonte.<Integer>get("ordre"),pointDescente.<Integer>get("ordre"));
            var debutJournee = date.atStartOfDay();
            var finJournee = date.plusDays(1).atStartOfDay();
            var conditionDate = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("dateDepart"),debutJournee),
                                criteriaBuilder.lessThan(root.get("dateDepart"),finJournee));
            var conditionStatut = criteriaBuilder.equal(root.get("statut"), StatutTrajet.PUBLIE);

            Subquery<Long> sousRequete = query.subquery(Long.class);
            Root<Reservation> reservation = sousRequete.from(Reservation.class);
            sousRequete.select(criteriaBuilder.coalesce(criteriaBuilder.sumAsLong(reservation.<Integer>get("nombrePlaces")), 0L));
            sousRequete.where(criteriaBuilder.equal(reservation.get("trajet").get("id"), root.get("id")),
                    criteriaBuilder.equal(reservation.get("statut"), StatutReservation.CONFIRMEE));
            var conditionPlaces = criteriaBuilder.lessThan(sousRequete, criteriaBuilder.toLong(root.get("placesProposees")));
            query.distinct(true);
            return criteriaBuilder.and(conditionDepart, conditionArret, conditionOrdre, conditionDate, conditionStatut, conditionPlaces);
        };
    }
}

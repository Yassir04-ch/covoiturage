package ma.covoiturage.covoiturage.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long id;
    private Integer nombrePlaces;
    private BigDecimal montantTotal;
    private LocalDateTime dateReservation;
    private String statut;
    private UUID passagerId;
    private Long trajetId;
    private PointArretResponse pointMontee;
    private PointArretResponse pointDescente;
}

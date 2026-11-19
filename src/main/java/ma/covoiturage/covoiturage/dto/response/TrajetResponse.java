package ma.covoiturage.covoiturage.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetResponse {
    private Long id;
    private LocalDateTime dateDepart;
    private Integer placesProposees;
    private String statut;
    private UUID conducteurId;
    private Long vehiculeId;
    private List<PointArretResponse> pointArrets;
}

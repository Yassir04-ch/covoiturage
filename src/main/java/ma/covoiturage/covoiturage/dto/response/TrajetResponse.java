package ma.covoiturage.covoiturage.dto.response;

import lombok.*;

import java.time.LocalDateTime;
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
}

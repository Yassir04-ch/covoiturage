package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetCreateRequest {
    @NotNull(message = "Date départ et obligatoire")
    private LocalDateTime dateDepart;
    @NotNull(message = "places proposees et obligatoire")
    @Min(value = 1,message = "nombre de places doit étre supérieur à 0")
    private int placesProposees;
    @NotNull(message = "conducteur et obligatoire")
    private UUID conducteurId;
    @NotNull(message = "véhicule est obligatoire")
    private Long vehiculeId;
}

package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
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
    @NotEmpty
    @Size(min = 2 , message = "trajet doit avoir au minimum 2 points d'arrét")
    private List<PointArretCreateRequest> pointArrets;
}

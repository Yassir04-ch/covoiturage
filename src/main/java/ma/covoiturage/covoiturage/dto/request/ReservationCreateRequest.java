package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateRequest {
    @NotNull(message = "passager et obligatoire")
    private UUID passagerId;
    @NotNull(message = "nombre des places et obligatoire")
    @Min(value = 1 , message = "minumum places et 1")
    private Integer nombrePlaces;
    @NotNull(message = "trajet et obligatoire")
    private long trajetId;
    @NotNull(message = "pointMontee et obligatoire")
    private long pointMonteeId;
    @NotNull(message = "pointDescente et obligatoire")
    private long pointDescenteId;
}

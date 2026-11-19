package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointArretCreateRequest {
    @NotBlank(message = "ville et obligatoire")
    private String ville;
    @NotNull(message = "Order et Obligatoire")
    @Min(value = 1 , message = "le order minimum et 1")
    private Integer ordre;
    @NotNull(message = "Heure de passage et obligatoire")
    private LocalDateTime heurePassage;
    @NotNull(message = "prix depuis depart et obligatoire")
    private BigDecimal prixDepuisDepart;

}

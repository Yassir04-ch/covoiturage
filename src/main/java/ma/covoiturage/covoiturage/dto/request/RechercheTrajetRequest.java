package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RechercheTrajetRequest {
    @NotBlank(message = "ville de depart est obligatoire ")
    private String villeDepart;
    @NotBlank(message = "ville de arrivee est obligatoire ")
    private String villeArrivee;
    @NotNull(message = "la date de trajet et obligatoire")
    private LocalDate date;
}

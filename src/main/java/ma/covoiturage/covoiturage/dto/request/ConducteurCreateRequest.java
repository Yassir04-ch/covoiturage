package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ConducteurCreateRequest extends UserCreateRequest{
    @NotBlank(message = "Le numéro de permis est obligatoire")
    private String numeroPermis;
}

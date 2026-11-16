package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConducteurCreateRequest extends UserCreateRequest{
    @NotBlank(message = "Le numéro de permis est obligatoire")
    private String numeroPermis;
}

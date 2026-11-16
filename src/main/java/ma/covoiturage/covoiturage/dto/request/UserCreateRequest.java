package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le prenom et obligatoire")
    private String prenom;
    @NotBlank(message = "email est obligatoire")
    @Email
    private String email;
    @NotBlank(message = "telephone et obligatoire")
    @Pattern(regexp = "^\\+?[0-9 ]{8,15}$", message = "telephone invalide")
    private String telephone;

}

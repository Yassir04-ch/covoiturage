package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
    @Size(min = 2 ,message = "nom minimum deux caracter")
    private String nom;
    @Size(min = 2 ,message = "prenom minimum deux caracter")
    private String prenom;
    @Email
    private String email;
    @Pattern(regexp = "^\\+?[0-9 ]{8,15}$", message = "telephone invalide")
    private String telephone;
}

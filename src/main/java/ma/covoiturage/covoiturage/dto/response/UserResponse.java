package ma.covoiturage.covoiturage.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;
    private String numeroPermis;
}
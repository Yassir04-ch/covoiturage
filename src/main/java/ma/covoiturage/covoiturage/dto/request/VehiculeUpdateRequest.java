package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculeUpdateRequest {
    private String marque;
    private String modele;
    private String immatriculation;
    private String couleur;
    @Min(value = 1 , message = "La capacité minimale est 1")
    @Max(value = 8 ,message = "La capacité maximale est 8")
    private Integer capacite;
}

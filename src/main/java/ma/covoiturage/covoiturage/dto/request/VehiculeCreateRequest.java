package ma.covoiturage.covoiturage.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.covoiturage.covoiturage.entity.Conducteur;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculeCreateRequest {
    @NotBlank(message = "La marque est obligatoire")
    private String marque;
    @NotBlank(message = "Le modéle est obligatoire")
    private String modele;
    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;
    private String couleur;
    @NotNull(message = "capacité est obligatoire")
    @Min(value = 1 , message = "La capacité minimale est 1")
    @Max(value = 8 ,message = "La capacité maximale est 8")
    private Integer capacite;
    @NotNull(message = "Le conducteur et obligatoire")
    private UUID conducteurId;
}

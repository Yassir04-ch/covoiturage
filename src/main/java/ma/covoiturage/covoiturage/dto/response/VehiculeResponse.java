package ma.covoiturage.covoiturage.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ma.covoiturage.covoiturage.entity.Conducteur;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculeResponse{
    private long id;
    private String marque;
    private String modele;
    private String immatriculation;
    private String couleur;
    private Integer capacite;
    private UUID conducteurId;
}

package ma.covoiturage.covoiturage.dto.response;

import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointArretResponse {
    private Long id;
    private String ville;
    private Integer ordre;
    private LocalDateTime heurePassage;
    private BigDecimal prixDepuisDepart;
}

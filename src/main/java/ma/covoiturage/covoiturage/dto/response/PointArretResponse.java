package ma.covoiturage.covoiturage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
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

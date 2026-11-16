package ma.covoiturage.covoiturage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "point_arrets")
@Getter
@Setter
@NoArgsConstructor
public class PointArret{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private Integer ordre;

    @Column(nullable = false)
    private LocalDateTime heurePassage;

    @Column(nullable = false)
    private BigDecimal prixDepuisDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id",nullable = false)
    private Trajet trajet;

    @OneToMany(mappedBy = "pointMontee")
    private List<Reservation> reservationsMontee = new ArrayList<>();

    @OneToMany(mappedBy = "pointDescente")
    private List<Reservation> reservationsDescente = new ArrayList<>();
}

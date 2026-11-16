package ma.covoiturage.covoiturage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicules")
@Getter
@Setter
@NoArgsConstructor
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    private String couleur;

    @Min(1)
    @Max(8)
    @Column(nullable = false)
    private Integer capacite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conducteur_id" , nullable = false)
    private Conducteur conducteur;

    @OneToMany(mappedBy = "vehicule")
    private List<Trajet> trajets = new ArrayList<>();
}

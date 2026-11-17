package ma.covoiturage.covoiturage.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conducteurs")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Conducteur extends User{
    @Column(nullable = false)
    private String numeroPermis;

    @OneToMany(mappedBy = "conducteur")
    private List<Vehicule> vehicules = new ArrayList<>();

    @OneToMany(mappedBy = "conducteur")
    private List<Trajet> trajets = new ArrayList<>();

}

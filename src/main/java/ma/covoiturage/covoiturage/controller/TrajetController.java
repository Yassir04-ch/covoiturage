package ma.covoiturage.covoiturage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.RechercheTrajetRequest;
import ma.covoiturage.covoiturage.dto.request.TrajetCreateRequest;
import ma.covoiturage.covoiturage.dto.response.TrajetResponse;
import ma.covoiturage.covoiturage.service.TrajetService;
import ma.covoiturage.covoiturage.service.impl.TrajetServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trajets")
@RequiredArgsConstructor
@Tag(name = "Trajets" , description = "Gestion des trajets")
public class TrajetController {
    private final TrajetService trajetService;

    @PostMapping
    public ResponseEntity<TrajetResponse> createTrajet(@Valid @RequestBody TrajetCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(trajetService.createTrajet(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrajet(@PathVariable long id , @RequestParam UUID conducteurId){
        trajetService.deleteTrajet(id , conducteurId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrajetResponse> getTrajetById(@PathVariable long id){
        return ResponseEntity.ok(trajetService.getTrajetById(id));
    }

    @GetMapping("/conducteur/{conducteurId}")
    public ResponseEntity<List<TrajetResponse>> getTrajetsByConducteurId(@PathVariable UUID conducteurId){
        return ResponseEntity.ok(trajetService.getTrajetsByConducteurId(conducteurId));
    }

    @GetMapping()
    public ResponseEntity<List<TrajetResponse>> getAllTrajets(){
        return ResponseEntity.ok(trajetService.getAllTrajets());
    }

    @PatchMapping("/annuler/{id}")
    public ResponseEntity<TrajetResponse> annulerTrajet(@PathVariable long id , @RequestParam UUID conducteurId ){
        return ResponseEntity.ok(trajetService.annulerTrajet(id , conducteurId));
    }

    @PatchMapping("/terminer/{id}")
    public ResponseEntity<TrajetResponse> terminerTrajet(@PathVariable long id , @RequestParam UUID conducteurId){
        return ResponseEntity.ok(trajetService.terminerTrajet(id , conducteurId));
    }

    @PostMapping("/recherche")
    public ResponseEntity<List<TrajetResponse>> rechercherTrajets(@Valid @RequestBody RechercheTrajetRequest request){
        return ResponseEntity.ok(trajetService.rechercherTrajets(request));
    }
}

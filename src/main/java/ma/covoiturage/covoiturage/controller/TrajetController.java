package ma.covoiturage.covoiturage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.TrajetCreateRequest;
import ma.covoiturage.covoiturage.dto.response.TrajetResponse;
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
    private final TrajetServiceImp trajetServiceImp;

    @PostMapping
    public ResponseEntity<TrajetResponse> createTrajet(@Valid @RequestBody TrajetCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(trajetServiceImp.createTrajet(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrajet(@PathVariable long id , @RequestParam UUID conducteurId){
        trajetServiceImp.deleteTrajet(id , conducteurId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrajetResponse> getTrajetById(@PathVariable long id){
        return ResponseEntity.ok(trajetServiceImp.getTrajetById(id));
    }

    @GetMapping("/conducteur/{conducteurId}")
    public ResponseEntity<List<TrajetResponse>> getTrajetsByConducteurId(@PathVariable UUID conducteurId){
        return ResponseEntity.ok(trajetServiceImp.getTrajetsByConducteurId(conducteurId));
    }

    @GetMapping()
    public ResponseEntity<List<TrajetResponse>> getAllTrajets(){
        return ResponseEntity.ok(trajetServiceImp.getAllTrajets());
    }
}

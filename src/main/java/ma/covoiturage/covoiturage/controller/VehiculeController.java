package ma.covoiturage.covoiturage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.VehiculeCreateRequest;
import ma.covoiturage.covoiturage.dto.request.VehiculeUpdateRequest;
import ma.covoiturage.covoiturage.dto.response.VehiculeResponse;
import ma.covoiturage.covoiturage.service.VehiculeService;
import ma.covoiturage.covoiturage.service.impl.VehiculeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
@Tag(name = "Vehicules" , description = "Gestion des vihicules")
public class VehiculeController {
    private final VehiculeService vehiculeService;

    @PostMapping
    public ResponseEntity<VehiculeResponse> createVehicule(@Valid @RequestBody VehiculeCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.createVehicule(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VehiculeResponse> updateVehicule(@PathVariable long id, @RequestParam UUID conducteurId ,@Valid @RequestBody VehiculeUpdateRequest request){
        return ResponseEntity.ok(vehiculeService.updateVehicule(id , conducteurId , request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable long id){
        vehiculeService.deleteVehicule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeResponse> getVehiculeById(@PathVariable long id){
        return ResponseEntity.ok(vehiculeService.getVehiculeById(id));
    }

    @GetMapping("/conducteur/{conducteurId}")
    public ResponseEntity<List<VehiculeResponse>> getVehiculeById(@PathVariable UUID conducteurId){
        return ResponseEntity.ok(vehiculeService.getVehiculesByConducteurId(conducteurId));
    }

    @GetMapping
    public ResponseEntity<List<VehiculeResponse>> getVehiculeById(){
        return ResponseEntity.ok(vehiculeService.getAllVehicules());
    }

}

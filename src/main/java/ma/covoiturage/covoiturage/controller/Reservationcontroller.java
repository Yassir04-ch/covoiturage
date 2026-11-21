package ma.covoiturage.covoiturage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.covoiturage.covoiturage.dto.request.ReservationCreateRequest;
import ma.covoiturage.covoiturage.dto.response.ReservationResponse;
import ma.covoiturage.covoiturage.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations" , description = "Gestion des reservations")
public class Reservationcontroller {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> annulerReservation(@PathVariable long id , @RequestParam UUID passagerId){
        return  ResponseEntity.ok(reservationService.annulerReservation(id , passagerId));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(){
        return ResponseEntity.ok(reservationService.getAllReservation());
    }

    @GetMapping("/passager/{passagerId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByPassager(@PathVariable UUID passagerId){
        return ResponseEntity.ok(reservationService.getReservationsByPassager(passagerId));
    }

    @GetMapping("/trajet/{trajetId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByTrajet(@PathVariable long trajetId){
        return ResponseEntity.ok(reservationService.getReservationsByTrajet(trajetId));
    }
}

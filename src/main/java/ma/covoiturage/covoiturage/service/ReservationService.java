package ma.covoiturage.covoiturage.service;

import ma.covoiturage.covoiturage.dto.request.ReservationCreateRequest;
import ma.covoiturage.covoiturage.dto.response.ReservationResponse;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    ReservationResponse createReservation(ReservationCreateRequest request);
    ReservationResponse annulerReservation(long reservationId , UUID passagerId);
    List<ReservationResponse> getAllReservation();
    List<ReservationResponse> getReservationsByPassager(UUID passagerId);
    List<ReservationResponse> getReservationsByTrajet(long trajetId);

}

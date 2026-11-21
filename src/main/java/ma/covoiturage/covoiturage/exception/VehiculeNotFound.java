package ma.covoiturage.covoiturage.exception;

public class VehiculeNotFound extends ResourceNotFoundException {
    public VehiculeNotFound(String message) {
        super(message);
    }
}

package ma.covoiturage.covoiturage.exception;

public class TrajetNotFound extends ResourceNotFoundException {
    public TrajetNotFound(String message) {
        super(message);
    }
}

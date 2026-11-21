package ma.covoiturage.covoiturage.exception;

public class PoinArretNotFound extends ResourceNotFoundException {
    public PoinArretNotFound(String message) {
        super(message);
    }
}

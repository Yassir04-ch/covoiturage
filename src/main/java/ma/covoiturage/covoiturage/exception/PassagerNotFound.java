package ma.covoiturage.covoiturage.exception;

public class PassagerNotFound extends ResourceNotFoundException{
    public PassagerNotFound(String message) {
        super(message);
    }
}

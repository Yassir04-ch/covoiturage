package ma.covoiturage.covoiturage.exception;

public class ConducteurNotFound extends ResourceNotFoundException{
    public ConducteurNotFound(String message) {super(message);}
}

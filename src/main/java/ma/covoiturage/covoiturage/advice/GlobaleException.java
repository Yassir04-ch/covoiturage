package ma.covoiturage.covoiturage.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ma.covoiturage.covoiturage.dto.response.ErrorResponse;
import ma.covoiturage.covoiturage.exception.ConflictException;
import ma.covoiturage.covoiturage.exception.ForbiddenException;
import ma.covoiturage.covoiturage.exception.InvalideException;
import ma.covoiturage.covoiturage.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobaleException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex , HttpServletRequest request){
        return build(HttpStatus.NOT_FOUND , ex.getMessage() , request);
    }

    @ExceptionHandler(InvalideException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(InvalideException ex , HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST , ex.getMessage() , request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex , HttpServletRequest request){
        return build(HttpStatus.FORBIDDEN , ex.getMessage() , request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex , HttpServletRequest request){
        return build(HttpStatus.CONFLICT , ex.getMessage() , request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).findFirst().orElse("Erreur de validation");
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest req) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(body);
    }
}

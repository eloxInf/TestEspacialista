package cl.rest.especialista.integracion.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author fsepulvb
 */
@RestControllerAdvice
public class GenericExceptionHandler {
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(GenericException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(ex.getMessage())
            .status(ex.getHttpStatus())
            .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}

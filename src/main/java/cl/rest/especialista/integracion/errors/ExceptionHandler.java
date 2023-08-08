package cl.rest.especialista.integracion.errors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.rest.especialista.integracion.dto.ResponseError;

/**
 * @author avenegas
 *
 */
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({EmailExistException.class})
    public ResponseEntity<ResponseError> emailExist(HttpServletRequest request, Exception exception) {
        ResponseError errorDto = new ResponseError();
        errorDto.setMessage(exception.getMessage());
        
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler({RequestDataException.class})
    public ResponseEntity<ResponseError> requestDateError(HttpServletRequest request, Exception exception) {
        ResponseError errorDto = new ResponseError();
        errorDto.setMessage(exception.getMessage());
        
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler({TokenException.class})
    public ResponseEntity<ResponseError> tokenError(HttpServletRequest request, Exception exception) {
        ResponseError errorDto = new ResponseError();
        errorDto.setMessage(exception.getMessage());
        
        return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
    }
    
    
    @org.springframework.web.bind.annotation.ExceptionHandler({GenericException.class})
    public ResponseEntity<ResponseError> GenericError(HttpServletRequest request, Exception exception) {
        ResponseError errorDto = new ResponseError();
        errorDto.setMessage(exception.getMessage());
        
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    @org.springframework.web.bind.annotation.ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ResponseError> userNotFoundException(HttpServletRequest request, Exception exception) {
        ResponseError errorDto = new ResponseError();
        errorDto.setMessage(exception.getMessage());
        
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }
    
    
    
    
}

package cl.rest.especialista.integracion.errors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.rest.especialista.integracion.dto.ResponseError;


/**
 * @author avenegas
 *
 */
@RestControllerAdvice
public class ExceptionHandlerCustom {

	@ExceptionHandler({EmailExistException.class})
    public ResponseEntity<ResponseError> emailExist(HttpServletRequest request, Exception exception) {     
        return new ResponseEntity<>(ResponseError.builder().message(exception.getMessage()).build(), HttpStatus.CONFLICT);
    }
	
    @ExceptionHandler({RequestDataException.class})
    public ResponseEntity<ResponseError> requestDateError(HttpServletRequest request, Exception exception) {
        
        return new ResponseEntity<>(ResponseError.builder().message(exception.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler({TokenException.class})
    public ResponseEntity<ResponseError> tokenError(HttpServletRequest request, Exception exception) {
        return new ResponseEntity<>(ResponseError.builder().message(exception.getMessage()).build(), HttpStatus.UNAUTHORIZED);
    }
    
    
    @ExceptionHandler({GenericException.class})
    public ResponseEntity<ResponseError> GenericError(HttpServletRequest request, Exception exception) {
        return new ResponseEntity<>(ResponseError.builder().message(exception.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ResponseError> userNotFoundException(HttpServletRequest request, Exception exception) {
        return new ResponseEntity<>(ResponseError.builder().message(exception.getMessage()).build(), HttpStatus.NOT_FOUND);
    }
    
   
  
}

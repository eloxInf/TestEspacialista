package cl.bci.espacialista.integracion.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorUtil {
	
	
	public static final String getDetailError(BindingResult errors) {
		
		String errorDetail = "";
		
		if(errors.hasErrors()) {
	        for (FieldError error : errors.getFieldErrors()) {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                
                errorDetail = errorDetail + fieldName + "|" + errorMessage + " ";
            }
	
		}
		
		return errorDetail;
	}
	

}

package cl.rest.especialista.integracion.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import cl.rest.especialista.integracion.errors.RequestDataException;

/**
 * @author avenegas
 *
 */
public class ErrorUtil {
	
	
	
	/**
	 * @param requestUser
	 * @param errors
	 */
	public static void validateError(BindingResult errors) {	
		String errorsDetail = ErrorUtil.getDetailError(errors);

		if(!errorsDetail.isEmpty()) {	
			throw new RequestDataException(errorsDetail);
		}
	}
	
	
	/**
	 * @param errors
	 * @return
	 */
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

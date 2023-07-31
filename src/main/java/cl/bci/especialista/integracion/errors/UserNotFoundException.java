package cl.bci.especialista.integracion.errors;

/**
 * @author avenegas
 *
 */
public class UserNotFoundException extends RuntimeException  {
	

	private static final long serialVersionUID = 1L;
	
	
	public UserNotFoundException(String detail) {
		super(detail);
	}
	
}

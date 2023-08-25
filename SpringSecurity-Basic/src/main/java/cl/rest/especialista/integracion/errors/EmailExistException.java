package cl.rest.especialista.integracion.errors;

/**
 * @author avenegas
 *
 */
public class EmailExistException extends RuntimeException  {
	

	private static final long serialVersionUID = 1L;
	
	
	public EmailExistException(String detail) {
		super(detail);
	}
	
}

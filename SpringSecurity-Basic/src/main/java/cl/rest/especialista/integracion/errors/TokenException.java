package cl.rest.especialista.integracion.errors;

/**
 * @author avenegas
 *
 */
public class TokenException extends RuntimeException  {
	

	private static final long serialVersionUID = 1L;
	
	
	public TokenException(String detail) {
		super(detail);
	}
	
}

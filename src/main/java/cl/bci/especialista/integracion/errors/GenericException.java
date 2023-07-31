package cl.bci.especialista.integracion.errors;

/**
 * @author avenegas
 *
 */
public class GenericException extends RuntimeException  {
	

	private static final long serialVersionUID = 1L;

	public GenericException(String detail) {
		super(detail);
	}
	
}

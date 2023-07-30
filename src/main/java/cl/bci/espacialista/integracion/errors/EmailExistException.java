package cl.bci.espacialista.integracion.errors;

public class EmailExistException extends RuntimeException  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EmailExistException(String detail) {
		super(detail);
	}
	
}

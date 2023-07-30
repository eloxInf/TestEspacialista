package cl.bci.espacialista.integracion.errors;

public class RequestDataException extends RuntimeException  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public RequestDataException(String detail) {
		super(detail);
	}
	
}

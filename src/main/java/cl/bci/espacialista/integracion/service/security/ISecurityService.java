package cl.bci.espacialista.integracion.service.security;

import java.util.Map;

public interface ISecurityService {

	String createToken(Map<String, Object> propertyUser, String user);

	boolean isValidToken(String token);
}

package cl.bci.espacialista.integracion.service.security;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;

public interface ISecurityService {

	String createToken(Map<String, Object> propertyUser, String user);
}

package cl.bci.espacialista.integracion.service.security;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;

public interface ISecurityService {

	public String generateToken(String username, String pass);

	public String createToken(Map<String, Object> claims, String subject);

	public String extractUsername(String token);

	public boolean isTokenExpired(String token);

	public Date extractExpirationDate(String token);

	public Claims extractAllClaims(String token);

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	public boolean validateToken(String token, String username);
}

package cl.bci.espacialista.integracion.service.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SecurityService implements ISecurityService {
	
    private final String secret = "secreto-muy-seguro"; // Cambia esto por tu propia clave secreta
    private final long expirationMs = 3600000;
	
	
    @Override
    public String generateToken(String username, String pass) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    } 
    
    @Override
	public String createToken(Map<String, Object> claims, String user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    
    @Override
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
	public boolean isTokenExpired(String token) {
        Date expiration = extractExpirationDate(token);
        return expiration.before(new Date());
    }

    @Override
	public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    @Override
	public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    
    @Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    @Override
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

}

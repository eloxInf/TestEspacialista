package cl.bci.espacialista.integracion.service.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.bci.espacialista.integracion.errors.TokenException;
import cl.bci.espacialista.integracion.service.IUserServices;
import cl.bci.espacialista.integracion.util.ValuesFromYmlUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SecurityService implements ISecurityService {
	
	@Autowired
	private ValuesFromYmlUtil valuesFromYmlUtil;
	
	

    private final long expirationMs = 3600000;
	
    
    @Override
	public String createToken(Map<String, Object> propertyUser, String user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMs);
        
        String keyEncript = valuesFromYmlUtil.getKeyEncipt();

        return Jwts.builder()
                .setClaims(propertyUser)
                .setSubject(user)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, keyEncript)
                .compact();
    }
    
  
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

   
	public boolean isTokenExpired(String token) {
        Date expiration = extractExpirationDate(token);
        return expiration.before(new Date());
    }
	
	   
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

  
	private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


   
	private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(valuesFromYmlUtil.getKeyEncipt()).parseClaimsJws(token).getBody();
    }
    
  
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    


}

package cl.rest.especialista.integracion.service.security;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.rest.especialista.integracion.errors.TokenException;
import cl.rest.especialista.integracion.util.ValuesFromYmlUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author avenegas
 *
 */
@Service
public class SecurityService implements ISecurityService {
	
	protected static final Logger log = LoggerFactory.getLogger(SecurityService.class);

	@Autowired
	private ValuesFromYmlUtil valuesFromYmlUtil;

	/**
	 * Genera token
	 */
	@Override
	public String createToken(Map<String, Object> propertyUser, String user) {
		Date now = new Date();
		Long expi = Long.parseLong(valuesFromYmlUtil.getTokenExpiration());
		Date expirationDate = new Date(now.getTime() + expi);

		String keyEncript = valuesFromYmlUtil.getKeyEncipt();

		return Jwts.builder().setClaims(propertyUser).setSubject(user).setIssuedAt(now).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS256, keyEncript).compact();
	}

	/**
	 * Valida si token es correcto
	 */
	@Override
	public boolean isValidToken(String token) {
		boolean status = false;
		try {
			Date expiration = extractExpirationDate(token);
			Date nowDate = new Date();
			
			long timeDifferenceMillis = expiration.getTime() - nowDate.getTime();
			
			status = (timeDifferenceMillis>=0);
			

		} catch (Exception e) {
			log.error("[SecurityService]-[isValidToken] error : " + e.getMessage());
			throw new TokenException("Error en token");
		}

		
		return status;
	}

	/**
	 * @param token
	 * @return
	 */
	private Date extractExpirationDate(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * @param token
	 * @return
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(valuesFromYmlUtil.getKeyEncipt()).parseClaimsJws(token).getBody();
	}

	/**
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

}

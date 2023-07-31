package cl.bci.especialista.integracion.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author avenegas
 *
 */
@Component
@Data
public class ValuesFromYmlUtil {
	
	@Value("${validation.user.email}")
	private String emailExpresion;
	
	@Value("${security.keyEncript}")
	private String keyEncipt;
	
	@Value("${security.timeToken}")
	private String tokenExpiration;

}

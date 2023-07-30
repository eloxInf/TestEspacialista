package cl.bci.espacialista.integracion.util;

import java.util.UUID;
import java.util.regex.Pattern;
public class CommonUtil {

    public static final String generateUUID() {
    	
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        return uuidString;
    }
    
    
    public static boolean validateRegexPattern(String fieldValue, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(fieldValue).matches();
    }

}

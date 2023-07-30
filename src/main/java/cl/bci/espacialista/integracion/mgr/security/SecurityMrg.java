package cl.bci.espacialista.integracion.mgr.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cl.bci.espacialista.integracion.errors.TokenException;
import cl.bci.espacialista.integracion.service.IUserServices;
import cl.bci.espacialista.integracion.service.security.ISecurityService;

@Component
public class SecurityMrg implements ISecurity {
	
	@Autowired
	private ISecurityService securityService;
	
	@Autowired
	private IUserServices userServices;
	
	
	@Override
	public String loginUser(String email, String pass) {
		
		validateUserPass(email, pass);
		Date dateNow = new Date();
		
		// Deberia existir una logica de permisos de usuario en persistencia.
		Map<String, Object> propertyUser = new HashMap<String, Object>();
		propertyUser.put("typeUser", "SA");
		
		String tokenUser = securityService.createToken(propertyUser, email);
		
		userServices.updateLastLogin(email, tokenUser,  dateNow);
		
		return tokenUser;
		
	}
	
	
    
    public void validateUserPass(String user, String pass) {
    	Boolean exist = userServices.checkEmailAndPass(user, pass);
    	
    	if(!exist) {
    		// Todo Se podria cambiar por nombre mas generico.
    		throw new TokenException("error user/pass");
    	}
    	

    } 
    
    
    

}

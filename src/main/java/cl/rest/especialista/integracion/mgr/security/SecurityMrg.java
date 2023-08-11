package cl.rest.especialista.integracion.mgr.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cl.rest.especialista.integracion.errors.TokenException;
import cl.rest.especialista.integracion.service.IUserServices;
import cl.rest.especialista.integracion.service.security.ISecurityService;

@Component
public class SecurityMrg implements ISecurity {
	
	@Autowired
	private ISecurityService securityService;
	
	@Autowired
	private IUserServices userServices;
	
	
	/**
	 * Login de usuario
	 */
	@Override
	public String loginUser(String email, String pass) {
		
		validateUserPass(email, pass);
		Date dateNow = new Date();
	
		Map<String, Object> propertyUser = new HashMap<String, Object>();
		propertyUser.put("typeUser", "SA");
		
		String tokenUser = securityService.createToken(propertyUser, email);
		
		userServices.updateLastLogin(email, tokenUser,  dateNow);
		
		return tokenUser;
		
	}
	
	
    
    /**
     * @param user
     * @param pass
     */
    public void validateUserPass(String user, String pass) {
    	
    	Boolean exist = userServices.checkEmailAndPass(user, pass);
    	
    	if(!exist) {
    		throw new TokenException("error user/pass");
    	}
    	

    } 
    
    
    

}
